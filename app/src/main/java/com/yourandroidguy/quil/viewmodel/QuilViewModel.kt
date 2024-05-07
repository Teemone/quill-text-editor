package com.yourandroidguy.quil.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yourandroidguy.quil.data.Note
import com.yourandroidguy.quil.database.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

//todo:
// 1. Save a text file to internal/external storage

class QuilViewModel(
    private val noteDao: NoteDao
): ViewModel() {

    fun addOrUpdateNote(note: Note) {
        viewModelScope.launch {
            noteDao.upsertNote(note)
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }

    fun getAllRecords(): Flow<List<Note>> {
        return noteDao.getAllRecords()
    }

    fun shareNote(note: Note): Intent{
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "${note.title}\n\n${note.body}")
            type = "text/plain"
        }

        return Intent.createChooser(intent, null)
    }
}

@Suppress("UNCHECKED_CAST")
class QuilViewModelFactory(
    private val noteDao: NoteDao
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(modelClass){
            QuilViewModel::class.java -> {
                QuilViewModel(noteDao)
            }
            else -> throw IllegalArgumentException("Unknown class $modelClass")
        } as T
    }
}