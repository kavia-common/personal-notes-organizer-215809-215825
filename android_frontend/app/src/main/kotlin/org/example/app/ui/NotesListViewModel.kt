package org.example.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import org.example.app.data.Note
import org.example.app.data.NoteRepository

/**
 * PUBLIC_INTERFACE
 * ViewModel exposing the list of notes.
 */
class NotesListViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = NoteRepository.getInstance(app)
    val notes: LiveData<List<Note>> = repo.observeNotes()
}
