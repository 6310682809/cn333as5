package com.example.phonebook.database

import com.example.phonebook.domain.model.TagModel
import com.example.phonebook.domain.model.NEW_CONTACT_ID
import com.example.phonebook.domain.model.ContactModel

class DbMapper {
    // Create list of ContactModels by pairing each contact with a tag
    fun mapContacts(
        contactDbModels: List<ContactDbModel>,
        tagDbModels: Map<Long, TagDbModel>
    ): List<ContactModel> = contactDbModels.map {
        val tagDbModel = tagDbModels[it.tagId]
            ?: throw RuntimeException("Tag for tagId: ${it.tagId} was not found. Make sure that all tags are passed to this method")

        mapContact(it, tagDbModel)
    }

    // convert ContactDbModel to ContactModel
    fun mapContact(contactDbModel: ContactDbModel, tagDbModel: TagDbModel): ContactModel {
        val tag = mapTag(tagDbModel)
//        val isCheckedOff = with(contactDbModel) { if (canBeCheckedOff) isCheckedOff else null }
        return with(contactDbModel) { ContactModel(id, name, phone, tag) }
    }

    // convert list of TagDdModels to list of TagModels
    fun mapTags(tagDbModels: List<TagDbModel>): List<TagModel> =
        tagDbModels.map { mapTag(it) }

    // convert TagDbModel to TagModel
    fun mapTag(TagDbModel: TagDbModel): TagModel =
        with(TagDbModel) { TagModel(id, name, hex) }

    // convert ContactModel back to ContactDbModel
    fun mapDbContact(contact: ContactModel): ContactDbModel =
        with(contact) {
            if (id == NEW_CONTACT_ID)
                ContactDbModel(
                    name = name,
                    phone = phone,
                    tagId = tag.id,
                    isInTrash = false
                )
            else
                ContactDbModel(id, name, phone, tag.id, false)
        }
}