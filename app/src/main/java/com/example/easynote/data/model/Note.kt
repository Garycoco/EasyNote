package com.example.easynote.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("note") val note: String,
    @ColumnInfo("created_at") val createdAt: Date,
    @ColumnInfo("is_favourite") val isFavourite: Boolean = false
)
