package com.example.phonebook.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.phonebook.domain.model.TagModel
import com.example.phonebook.domain.model.ContactModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Repository(
    private val contactDao: ContactDao,
    private val tagDao: TagDao,
    private val dbMapper: DbMapper
) {

    // Working Contacts
    private val contactsNotInTrashLiveData: MutableLiveData<List<ContactModel>> by lazy {
        MutableLiveData<List<ContactModel>>()
    }

    fun getAllContactsNotInTrash(): LiveData<List<ContactModel>> = contactsNotInTrashLiveData

    // Deleted Contacts
    private val contactsInTrashLiveData: MutableLiveData<List<ContactModel>> by lazy {
        MutableLiveData<List<ContactModel>>()
    }

    fun getAllContactsInTrash(): LiveData<List<ContactModel>> = contactsInTrashLiveData

    init {
        initDatabase(this::updateContactsLiveData)
    }

    /**
     * Populates database with tags if it is empty.
     */
    private fun initDatabase(postInitAction: () -> Unit) {
        GlobalScope.launch {
            // Prepopulate tags
            val tags = TagDbModel.DEFAULT_TAGS.toTypedArray()
            val dbTags = tagDao.getAllSync()
            if (dbTags.isNullOrEmpty()) {
                tagDao.insertAll(*tags)
            }

            // Prepopulate contacts
            val contacts = ContactDbModel.DEFAULT_CONTACTS.toTypedArray()
            val dbContacts = contactDao.getAllSync()
            if (dbContacts.isNullOrEmpty()) {
                contactDao.insertAll(*contacts)
            }

            postInitAction.invoke()
        }
    }

    // get list of working contacts or deleted contacts
    private fun getAllContactsDependingOnTrashStateSync(inTrash: Boolean): List<ContactModel> {
        val tagDbModels: Map<Long, TagDbModel> = tagDao.getAllSync().map { it.id to it }.toMap()
        val dbContacts: List<ContactDbModel> =
            contactDao.getAllSync().filter { it.isInTrash == inTrash }
        return dbMapper.mapContacts(dbContacts, tagDbModels)
    }

    fun insertContact(contact: ContactModel) {
        contactDao.insert(dbMapper.mapDbContact(contact))
        updateContactsLiveData()
    }

    fun deleteContacts(contactIds: List<Long>) {
        contactDao.delete(contactIds)
        updateContactsLiveData()
    }

    fun moveContactToTrash(contactId: Long) {
        val dbContact = contactDao.findByIdSync(contactId)
        val newDbContact = dbContact.copy(isInTrash = true)
        contactDao.insert(newDbContact)
        updateContactsLiveData()
    }

    fun restoreContactsFromTrash(contactIds: List<Long>) {
        val dbContactsInTrash = contactDao.getContactsByIdsSync(contactIds)
        dbContactsInTrash.forEach {
            val newDbContact = it.copy(isInTrash = false)
            contactDao.insert(newDbContact)
        }
        updateContactsLiveData()
    }

    fun getAllTags(): LiveData<List<TagModel>> =
        Transformations.map(tagDao.getAll()) { dbMapper.mapTags(it) }

    private fun updateContactsLiveData() {
        contactsNotInTrashLiveData.postValue(getAllContactsDependingOnTrashStateSync(false))
        contactsInTrashLiveData.postValue(getAllContactsDependingOnTrashStateSync(true))
    }
}