package com.irfan.moviecatalog.ui.main.content

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.irfan.moviecatalog.data.remote.ApiService
import com.irfan.moviecatalog.data.remote.response.MovieItem
import com.irfan.moviecatalog.utils.MovieEndpoint

class MoviePagingSource(
    private val service: ApiService,
    private val endpoint: MovieEndpoint
) : PagingSource<Int, MovieItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = when (endpoint) {
                MovieEndpoint.NOW_PLAYING -> service.getNowPlayingMovies(nextPageNumber)
                MovieEndpoint.UPCOMING -> service.getUpcomingMovies(nextPageNumber)
                MovieEndpoint.POPULAR -> service.getPopularMovies(nextPageNumber)
                MovieEndpoint.TOP_RATED -> service.getTopRatedMovies(nextPageNumber)
            }
            LoadResult.Page(
                data = response.results,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (nextPageNumber == response.totalPages) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}