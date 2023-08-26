package com.irfan.moviecatalog.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.irfan.moviecatalog.data.remote.ApiService
import com.irfan.moviecatalog.data.remote.response.MovieItem
import com.irfan.moviecatalog.ui.main.content.MoviePagingSource
import com.irfan.moviecatalog.utils.MovieEndpoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class MovieRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getMoviesByEndpoint(endpoint: MovieEndpoint): Flow<PagingData<MovieItem>> {
        return Pager(PagingConfig(pageSize = 15)) {
            MoviePagingSource(apiService, endpoint)
        }.flow
    }
}