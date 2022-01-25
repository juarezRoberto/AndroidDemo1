package com.juarez.coppeldemo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juarez.coppeldemo.data.models.HeroEntity

@Database(entities = [HeroEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun heroDao(): HeroDao
}