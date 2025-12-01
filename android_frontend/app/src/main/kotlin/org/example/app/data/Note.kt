package org.example.app.data

/**
 * PUBLIC_INTERFACE
 * Data model representing a note.
 */
data class Note(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
)
