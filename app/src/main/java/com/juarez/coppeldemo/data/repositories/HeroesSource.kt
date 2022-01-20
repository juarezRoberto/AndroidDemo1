package com.juarez.coppeldemo.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.juarez.coppeldemo.domain.GetHeroesUseCase
import com.juarez.coppeldemo.data.models.Hero
import retrofit2.HttpException
import java.io.IOException

class HeroesSource(private val getHeroesUseCase: GetHeroesUseCase) : PagingSource<Int, Hero>() {
    override fun getRefreshKey(state: PagingState<Int, Hero>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hero> {

        return try {
            val nextPageNumber = params.key ?: 1
            val response = getHeroesUseCase(nextPageNumber)
            val heroes: ArrayList<Hero>
            if (response.isSuccess) heroes = response.data!! as ArrayList<Hero>
            else return LoadResult.Error(Throwable(response.message))

            LoadResult.Page(
                data = heroes,
                prevKey = null, // only fordward
                nextKey = nextPageNumber + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}