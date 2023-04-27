package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "tag_id") val tagId: Long,
    @ColumnInfo(name = "in_trash") val isInTrash: Boolean
) {
    companion object {
        val DEFAULT_CONTACTS = listOf(
            ContactDbModel(1, "Ann", "01111111111",1, false),
            ContactDbModel(2, "Bird", "0222222222",2, false),
            ContactDbModel(3, "Cat", "0333333333",3, false),
            ContactDbModel(4, "Dog", "0444444444",4, false),
            ContactDbModel(5, "Egg", "0555555555",5, false),
        )
    }
}