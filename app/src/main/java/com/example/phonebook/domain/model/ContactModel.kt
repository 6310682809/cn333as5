package com.example.phonebook.domain.model

const val NEW_CONTACT_ID = -1L

data class ContactModel(
    val id: Long = NEW_CONTACT_ID, // This value is used for new contacts
    val name: String = "",
    val phone: String = "",
    val tag: TagModel = TagModel.DEFAULT
)