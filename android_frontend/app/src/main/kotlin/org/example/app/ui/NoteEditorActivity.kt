package org.example.app.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import org.example.app.R

/**
 PUBLIC_INTERFACE
 Screen to create a new note or edit an existing one. Supports explicit save and delete with confirmation.
 Extras:
 - EXTRA_NOTE_ID (Long) optional, when provided loads an existing note.
 */
class NoteEditorActivity : AppCompatActivity() {

    private val vm: NoteEditorViewModel by viewModels()
    private var noteId: Long? = null

    private lateinit var toolbar: MaterialToolbar
    private lateinit var titleInput: EditText
    private lateinit var contentInput: EditText
    private lateinit var saveButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_note_editor)

        toolbar = findViewById(R.id.toolbar)
        titleInput = findViewById(R.id.titleInput)
        contentInput = findViewById(R.id.contentInput)
        saveButton = findViewById(R.id.saveButton)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteId = intent.getLongExtra(EXTRA_NOTE_ID, 0L).takeIf { it != 0L }
        vm.load(noteId)

        vm.noteLive.observe(this) { note ->
            if (note != null) {
                supportActionBar?.title = getString(R.string.edit_note)
                titleInput.setText(note.title)
                contentInput.setText(note.content)
            } else {
                supportActionBar?.title = getString(R.string.new_note)
            }
        }

        saveButton.setOnClickListener {
            saveNote()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_editor, menu)
        if (noteId == null) {
            menu.findItem(R.id.action_delete)?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_delete -> {
                confirmDelete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val title = titleInput.text?.toString()?.trim().orEmpty()
        val content = contentInput.text?.toString()?.trim().orEmpty()
        if (title.isEmpty()) {
            Snackbar.make(findViewById(R.id.root), getString(R.string.title_hint), Snackbar.LENGTH_SHORT)
                .setBackgroundTint(getColor(R.color.ocean_error)).show()
            titleInput.requestFocus()
            return
        }
        vm.save(noteId, title, content) { savedId ->
            noteId = savedId
            Snackbar.make(findViewById(R.id.root), getString(R.string.save), Snackbar.LENGTH_SHORT)
                .setBackgroundTint(getColor(R.color.ocean_secondary)).show()
            finish()
        }
    }

    private fun confirmDelete() {
        val id = noteId ?: return
        AlertDialog.Builder(this)
            .setTitle(R.string.confirm_delete_title)
            .setMessage(R.string.confirm_delete_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ ->
                vm.delete(id) {
                    finish()
                }
            }.show()
    }

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }
}
