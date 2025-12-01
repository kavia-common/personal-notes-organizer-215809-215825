package org.example.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import org.example.app.ui.NotesListActivity

/**
 * PUBLIC_INTERFACE
 * Launcher fallback activity which immediately routes to the Notes list.
 * This is kept to avoid breaking existing references, but the real UI starts at NotesListActivity.
 */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, NotesListActivity::class.java))
        finish()
    }
}
