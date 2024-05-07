package com.yourandroidguy.quil.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteQuery
import com.yourandroidguy.quil.data.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note")
    fun getAllRecords(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY date ASC")
    fun sortByDate(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY title ASC")
    fun sortByAlphabet(): Flow<List<Note>>
}