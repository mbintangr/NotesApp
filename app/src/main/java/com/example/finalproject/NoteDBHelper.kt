package com.example.finalproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class NoteDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "finalproject.db"
        private const val DATABASE_VERSION = 1
        private const val NOTES_TABLE_NAME = "notes"
        private const val COLUMN_NOTE_ID = "note_id"
        private const val COLUMN_NOTE_USER_ID = "user_id"
        private const val COLUMN_NOTE_TITLE= "title"
        private const val COLUMN_NOTE_DATE = "date"
        private const val COLUMN_NOTE_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createNotesQuery = "CREATE TABLE $NOTES_TABLE_NAME ($COLUMN_NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NOTE_USER_ID INTEGER, $COLUMN_NOTE_TITLE TEXT, $COLUMN_NOTE_DATE TEXT, $COLUMN_NOTE_CONTENT TEXT, FOREIGN KEY($COLUMN_NOTE_USER_ID) REFERENCES users(id))"
        db?.execSQL(createNotesQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $NOTES_TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }


}