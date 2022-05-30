package com.juarez.coppeldemo.di

import android.content.Context
import com.juarez.coppeldemo.api.HeroAPI
import com.juarez.coppeldemo.core.PreferencesManager
import com.juarez.coppeldemo.db.HeroDao
import com.juarez.coppeldemo.heroes.data.HeroRepository
import com.juarez.coppeldemo.heroes.data.HeroRepositoryImp
import com.juarez.coppeldemo.heroes.heroes.data.GetHeroesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }

    @Provides
    fun provideHeroRepository(
        heroAPI: HeroAPI,
        heroDao: HeroDao,
        getHeroesService: GetHeroesService,
    ): HeroRepository {
        return HeroRepositoryImp(Dispatchers.IO, heroAPI, heroDao, getHeroesService)
    }
}