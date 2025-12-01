package org.example.app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * PUBLIC_INTERFACE
 * Lightweight SQLite storage to avoid annotation processor requirements.
 * Provides CRUD similar to Room-backed repository.
 */
class SqliteNotesStore(context: Context) : SQLiteOpenHelper(context, "notes-db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "content TEXT NOT NULL," +
                "createdAt INTEGER NOT NULL," +
                "updatedAt INTEGER NOT NULL" +
            ")"
        )
        db.execSQL("CREATE INDEX idx_updatedAt ON notes(updatedAt DESC)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS notes")
        onCreate(db)
    }

    fun insert(title: String, content: String): Long {
        val now = System.currentTimeMillis()
        val cv = ContentValues().apply {
            put("title", title)
            put("content", content)
            put("createdAt", now)
            put("updatedAt", now)
        }
        return writableDatabase.insert("notes", null, cv)
    }

    fun update(id: Long, title: String, content: String) {
        val cv = ContentValues().apply {
            put("title", title)
            put("content", content)
            put("updatedAt", System.currentTimeMillis())
        }
        writableDatabase.update("notes", cv, "id = ?", arrayOf(id.toString()))
    }

    fun delete(id: Long) {
        writableDatabase.delete("notes", "id = ?", arrayOf(id.toString()))
    }

    fun getById(id: Long): Note? {
        val c = readableDatabase.query("notes", arrayOf("id","title","content","createdAt","updatedAt"),
            "id = ?", arrayOf(id.toString()), null, null, null)
        c.use {
            if (it.moveToFirst()) {
                return Note(
                    id = it.getLong(0),
                    title = it.getString(1),
                    content = it.getString(2),
                    createdAt = it.getLong(3),
                    updatedAt = it.getLong(4)
                )
            }
        }
        return null
    }

    fun getAll(): List<Note> {
        val list = mutableListOf<Note>()
        val c = readableDatabase.query(
            "notes",
            arrayOf("id","title","content","createdAt","updatedAt"),
            null, null, null, null, "updatedAt DESC"
        )
        c.use {
            while (it.moveToNext()) {
                list.add(
                    Note(
                        id = it.getLong(0),
                        title = it.getString(1),
                        content = it.getString(2),
                        createdAt = it.getLong(3),
                        updatedAt = it.getLong(4)
                    )
                )
            }
        }
        return list
    }
}
