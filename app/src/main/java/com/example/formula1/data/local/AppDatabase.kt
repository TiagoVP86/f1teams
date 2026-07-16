package com.example.formula1.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TeamEntity::class, DriverEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teamDao(): TeamDao
    abstract fun driverDao(): DriverDao

    companion object {
        fun build(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "f1.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
