package com.sri.androidmentorchat.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MessageEntity::class], version = 1, exportSchema = false)
abstract class MentorDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: MentorDatabase? = null

        fun getDatabase(context: Context): MentorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context,
                    klass = MentorDatabase::class.java,
                    name = "mentor_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}