package com.juarez.coppeldemo.di

import android.content.Context
import androidx.room.Room
import com.juarez.coppeldemo.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "myApp.db").build()
    }

    @Provides
    fun provideHeroDao(database: AppDatabase) = database.heroDao()
}