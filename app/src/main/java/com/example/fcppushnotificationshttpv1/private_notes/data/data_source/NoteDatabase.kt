package com.example.fcppushnotificationshttpv1.private_notes.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fcppushnotificationshttpv1.private_notes.data.data_source.NoteDao
import com.example.fcppushnotificationshttpv1.private_notes.domain.model.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase : RoomDatabase() {

    abstract val noteDate: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}