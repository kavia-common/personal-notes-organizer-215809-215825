package org.example.app.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * Repository providing a clean API for data operations on notes, backed by SQLiteOpenHelper.
 */
class NoteRepository private constructor(context: Context) {

    private val store = SqliteNotesStore(context)
    private val notesLive = MutableLiveData<List<Note>>(emptyList())
    private val ioScope = CoroutineScope(Dispatchers.IO)

    fun observeNotes(): LiveData<List<Note>> {
        // initial load
        ioScope.launch { notesLive.postValue(store.getAll()) }
        return notesLive
    }

    suspend fun getNote(id: Long): Note? = store.getById(id)

    suspend fun addNote(title: String, content: String): Long {
        val id = store.insert(title, content)
        refresh()
        return id
    }

    suspend fun updateNote(id: Long, title: String, content: String) {
        store.update(id, title, content)
        refresh()
    }

    suspend fun deleteNote(id: Long) {
        store.delete(id)
        refresh()
    }

    private fun refresh() {
        ioScope.launch {
            notesLive.postValue(store.getAll())
        }
    }

    companion object {
        @Volatile private var INSTANCE: NoteRepository? = null

        // PUBLIC_INTERFACE
        fun getInstance(context: Context): NoteRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NoteRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
