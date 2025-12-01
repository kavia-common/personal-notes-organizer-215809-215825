package org.example.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.example.app.R
import org.example.app.data.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 PUBLIC_INTERFACE
 Notes list screen that shows notes sorted by last modified and allows creating a new note.
 */
class NotesListActivity : AppCompatActivity() {

    private val vm: NotesListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyState: View
    private lateinit var fab: FloatingActionButton
    private lateinit var toolbar: MaterialToolbar
    private lateinit var searchInput: EditText

    private val adapter = NotesAdapter { note ->
        val intent = Intent(this, NoteEditorActivity::class.java)
        intent.putExtra(NoteEditorActivity.EXTRA_NOTE_ID, note.id)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notes_list)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.notes)

        recyclerView = findViewById(R.id.recyclerView)
        emptyState = findViewById(R.id.emptyState)
        fab = findViewById(R.id.fab)
        searchInput = findViewById(R.id.searchInput)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            val intent = Intent(this, NoteEditorActivity::class.java)
            startActivity(intent)
        }

        vm.notes.observe(this) { notes ->
            adapter.submitList(notes)
            updateEmptyState(notes.isEmpty())
        }

        searchInput.isEnabled = false // placeholder
    }

    private fun updateEmptyState(show: Boolean) {
        emptyState.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }
}

private class NotesAdapter(
    private val onClick: (Note) -> Unit
) : androidx.recyclerview.widget.ListAdapter<Note, NoteViewHolder>(
    object : androidx.recyclerview.widget.DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = android.view.LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class NoteViewHolder(
    itemView: android.view.View,
    private val onClick: (Note) -> Unit
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    private val title: android.widget.TextView = itemView.findViewById(R.id.title)
    private val content: android.widget.TextView = itemView.findViewById(R.id.content)
    private val updated: android.widget.TextView = itemView.findViewById(R.id.updated)

    fun bind(note: Note) {
        title.text = note.title
        content.text = truncate(note.content)
        updated.text = itemView.context.getString(R.string.last_updated, formatTime(note.updatedAt))
        itemView.setOnClickListener { onClick(note) }
    }

    private fun truncate(text: String, limit: Int = 120): String {
        return if (text.length <= limit) text else text.substring(0, limit).trimEnd() + "…"
    }

    private fun formatTime(time: Long): String {
        val sdf = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
        return sdf.format(Date(time))
    }
}
