package com.juarez.coppeldemo.data.remoteDataSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.data.repositories.HeroRepository
import com.juarez.coppeldemo.utils.Constants
import com.juarez.coppeldemo.utils.NetworkResponse
import retrofit2.HttpException
import java.io.IOException

class HeroesPagingSource(private val getUserService: GetUserService) :
    PagingSource<Int, Hero>() {
    override fun getRefreshKey(state: PagingState<Int, Hero>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hero> {

        return try {
            val nextPageNumber = params.key ?: 1
            val heroes = arrayListOf<Hero>()
            val response = getUserService(nextPageNumber)
            if (response is NetworkResponse.Success<*>) {
                response.data?.let { heroes.addAll(it) }
            }

            LoadResult.Page(
                data = heroes,
                prevKey = null, // only fordward
                nextKey = nextPageNumber + 1
            )
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: IOException) {
            LoadResult.Error(Exception(Constants.CONNECTION_ERROR))
        }
    }

}