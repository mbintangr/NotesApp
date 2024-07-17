package com.example.finalproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "finalproject.db"
        private const val DATABASE_VERSION = 1
        private const val USER_TABLE_NAME = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USER_EMAIL = "email"
        private const val COLUMN_USER_PASSWORD = "password"
        private const val COLUMN_USER_FULL_NAME = "full_name"

        private const val NOTES_TABLE_NAME = "notes"
        private const val COLUMN_NOTE_ID = "note_id"
        private const val COLUMN_NOTE_USER_ID = "user_id"
        private const val COLUMN_NOTE_TITLE= "title"
        private const val COLUMN_NOTE_DATE = "date"
        private const val COLUMN_NOTE_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersQuery = "CREATE TABLE $USER_TABLE_NAME ($COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_USER_EMAIL TEXT, $COLUMN_USER_PASSWORD TEXT, $COLUMN_USER_FULL_NAME TEXT)"
        db?.execSQL(createUsersQuery)
        val createNotesQuery = "CREATE TABLE $NOTES_TABLE_NAME ($COLUMN_NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NOTE_USER_ID INTEGER, $COLUMN_NOTE_TITLE TEXT, $COLUMN_NOTE_DATE TEXT, $COLUMN_NOTE_CONTENT TEXT, FOREIGN KEY($COLUMN_NOTE_USER_ID) REFERENCES users(id))"
        db?.execSQL(createNotesQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableUserQuery = "DROP TABLE IF EXISTS $USER_TABLE_NAME"
        db?.execSQL(dropTableUserQuery)
        val dropTableNoteQuery = "DROP TABLE IF EXISTS $NOTES_TABLE_NAME"
        db?.execSQL(dropTableNoteQuery)
        onCreate(db)
    }

    fun registerUser(user: User) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_EMAIL, user.email.trim())
            put(COLUMN_USER_PASSWORD, user.password.trim())
            put(COLUMN_USER_FULL_NAME, user.fullName)
        }

        db.insert(USER_TABLE_NAME, null, values)
        db.close()
    }

    fun loginAccount(email: String, password: String): Int {
        var id: Int = -1
        val db = readableDatabase
        val query = "SELECT $COLUMN_USER_ID FROM $USER_TABLE_NAME WHERE TRIM($COLUMN_USER_EMAIL) = ? AND $COLUMN_USER_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email.trim(), password))

        try {
            if (cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
            }
        } finally {
            cursor.close()
            db.close()
        }

        return id
    }

    fun checkEmail(email: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $USER_TABLE_NAME WHERE $COLUMN_USER_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))

        return try {
            cursor.count > 0
        } finally {
            db.close()
            cursor.close()
        }

    }

    fun getUserData(userId: Int): User {
        val db = readableDatabase
        val query = "SELECT * FROM $USER_TABLE_NAME WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        cursor.moveToFirst()

        return try {
            User(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)), cursor.getString(cursor.getColumnIndexOrThrow(
                COLUMN_USER_PASSWORD)), cursor.getString(cursor.getColumnIndexOrThrow(
                COLUMN_USER_FULL_NAME)))
        } finally {
            db.close()
            cursor.close()
        }
    }

    fun getNotesByUser(userId: Int): List<Note> {
        val db = readableDatabase
        val query = "SELECT * FROM $NOTES_TABLE_NAME WHERE $COLUMN_NOTE_USER_ID = $userId"
        val cursor = db.rawQuery(query, null)

        val notes = mutableListOf<Note>()

        while (cursor.moveToNext()) {
            val note = Note(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTE_ID)), cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTE_USER_ID)), cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_TITLE)), cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_DATE)), cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_CONTENT)))
            notes.add(note)
        }

        cursor.close()
        db.close()

        return notes
    }

    fun insertNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOTE_USER_ID, note.userId)
            put(COLUMN_NOTE_TITLE, note.title)
            put(COLUMN_NOTE_DATE, note.date)
            put(COLUMN_NOTE_CONTENT, note.content)
        }

        db.insert(NOTES_TABLE_NAME, null, values)
        db.close()
    }

    fun deleteNote(noteId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_NOTE_ID = ?"
        val whereArgs = arrayOf(noteId.toString())

        db.delete(NOTES_TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    fun updateNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOTE_TITLE, note.title)
            put(COLUMN_NOTE_DATE, note.date)
            put(COLUMN_NOTE_CONTENT, note.content)
        }

        val whereClause = "$COLUMN_NOTE_ID = ?"
        val whereArgs = arrayOf(note.noteId.toString())

        db.update(NOTES_TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getNoteById(noteId: Int): Note {
        val db = readableDatabase
        val query = "SELECT * FROM $NOTES_TABLE_NAME WHERE $COLUMN_NOTE_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(noteId.toString()))

        cursor.moveToFirst()

        return try {
            Note(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTE_ID)), cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTE_USER_ID)), cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_TITLE)), cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_DATE)), cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_CONTENT)))
        } finally {
            db.close()
            cursor.close()
        }
    }

}