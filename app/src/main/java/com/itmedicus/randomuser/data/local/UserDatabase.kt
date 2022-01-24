package com.itmedicus.randomuser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.itmedicus.randomuser.model.*


@Database(
    entities = [Result::class, AlarmTime::class,
        MultipleAlarm::class,
        TwoTimesAlarm::class,
        BmiRecord::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java, "user_database"
            ).allowMainThreadQueries().build()
    }
}