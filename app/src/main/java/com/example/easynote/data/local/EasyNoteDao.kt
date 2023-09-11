package com.example.easynote.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.easynote.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface EasyNoteDao {
    @Query("Select * From notes Order By created_at")
    fun getAllNotes(): Flow<List<Note>>

    @Query("Select * From notes Where id = :id Order By created_at")
    fun getNoteById(id: Long): Flow<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(vararg note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(vararg note: Note)

    @Query("Delete From notes Where id=:id")
    suspend fun deleteNote(id: Long)

    @Query("Select * From notes Where is_favourite = 1 Order By created_at Desc")
    fun getAllFavourites(): Flow<List<Note>>

}