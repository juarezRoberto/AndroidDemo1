package com.juarez.coppeldemo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juarez.coppeldemo.heroes.data.HeroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HeroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hero: HeroEntity)

    @Query("SELECT * from heroes_table")
    fun getAllHeroes(): Flow<List<HeroEntity>>

    @Query("DELETE from heroes_table")
    suspend fun deleteAllHeroes()

    @Query("DELETE FROM heroes_table WHERE heroId = :heroId")
    suspend fun removeFavoriteHero(heroId: Int)

    @Query("SELECT * FROM heroes_table WHERE heroId = :heroId")
    suspend fun getFavoriteHeroById(heroId: Int): HeroEntity?
}