package com.example.phonebook.database;

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "hex") val hex: String,
    @ColumnInfo(name = "name") val name: String
) {
    companion object {
        val DEFAULT_TAGS = listOf(
            TagDbModel(1, "#FFFFFF", "Mobile"),
            TagDbModel(2, "#E57373", "Home"),
            TagDbModel(3, "#F06292", "Work"),
            TagDbModel(4, "#CE93D8", "Fax"),
            TagDbModel(5, "#2196F3", "Toll-free"),
            TagDbModel(6, "#00ACC1", "Emergency"),
            TagDbModel(7, "#26A69A", "Personal"),
            TagDbModel(8, "#4CAF50", "Business"),
        )
        val DEFAULT_TAG = DEFAULT_TAGS[0]
    }
}
