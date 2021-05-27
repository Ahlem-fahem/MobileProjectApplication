package com.example.mobileprojectapplication.data.localdb


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mobileprojectapplication.data.localdb.dao.CitiesDao
import com.example.mobileprojectapplication.data.models.City


@Database(
    entities = [
        City::class
    ],
    version = 2,
    exportSchema = false
)

abstract class RoomLocalDb : RoomDatabase() {
    abstract fun citiesDao(): CitiesDao

    companion object {
        @Volatile
        private var INSTANCE: RoomLocalDb? = null
        private const val DATABASE_NAME = "room_db"

        fun getInstance(context: Context): RoomLocalDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(appContext: Context): RoomLocalDb {
            return Room.databaseBuilder(appContext, RoomLocalDb::class.java, DATABASE_NAME).allowMainThreadQueries()
                .fallbackToDestructiveMigration() // Data is cache, so it is OK to delete
                .build()
        }
    }
}