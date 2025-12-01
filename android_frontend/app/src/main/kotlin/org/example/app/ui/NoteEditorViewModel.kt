package org.example.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.app.data.Note
import org.example.app.data.NoteRepository

/**
 * PUBLIC_INTERFACE
 * ViewModel for creating and editing a note.
 */
class NoteEditorViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = NoteRepository.getInstance(app)

    val noteLive = MutableLiveData<Note?>()

    fun load(noteId: Long?) {
        if (noteId == null || noteId == 0L) {
            noteLive.value = null
            return
        }
        viewModelScope.launch {
            val note = repo.getNote(noteId)
            noteLive.postValue(note)
        }
    }

    fun save(noteId: Long?, title: String, content: String, onSaved: (Long) -> Unit) {
        viewModelScope.launch {
            val id = if (noteId == null || noteId == 0L) {
                repo.addNote(title, content)
            } else {
                repo.updateNote(noteId, title, content)
                noteId
            }
            onSaved(id)
        }
    }

    fun delete(noteId: Long, onDeleted: () -> Unit) {
        viewModelScope.launch {
            repo.deleteNote(noteId)
            onDeleted()
        }
    }
}
