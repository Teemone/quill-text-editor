package com.yourandroidguy.quil.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yourandroidguy.quil.data.Note

@Database(entities = [Note::class], version = 1)
abstract class QuilDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object{
        @Volatile
        private var INSTANCE: QuilDatabase? = null

        fun getInstance(context: Context): QuilDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    QuilDatabase::class.java,
                    "quil_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }

        }
    }
}