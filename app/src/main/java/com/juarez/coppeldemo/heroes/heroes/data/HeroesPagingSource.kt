package com.juarez.coppeldemo.heroes.heroes.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.juarez.coppeldemo.heroes.data.Hero
import com.juarez.coppeldemo.utils.Constants
import com.juarez.coppeldemo.utils.NetworkResponse
import retrofit2.HttpException
import java.io.IOException

class HeroesPagingSource(private val getHeroesService: GetHeroesService) :
    PagingSource<Int, Hero>() {
    override fun getRefreshKey(state: PagingState<Int, Hero>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hero> {

        try {
            val nextPageNumber = params.key ?: 1
            val heroes = arrayListOf<Hero>()
            val response = getHeroesService(nextPageNumber)
            if (response is NetworkResponse.Success) {
                response.data?.let { heroes.addAll(it) }
            }

            return LoadResult.Page(
                data = heroes,
                prevKey = null, // only fordward
                nextKey = nextPageNumber + 1
            )
        } catch (exception: Exception) {
            if (exception is HttpException) return LoadResult.Error(Throwable("un Http Exception"))
            if (exception is IOException) return LoadResult.Error(Throwable(Constants.CONNECTION_ERROR))

            return LoadResult.Error(exception)
        }
//        catch (exception: IOException) {
//            LoadResult.Error(Exception(Constants.CONNECTION_ERROR))
//        }
    }

}