package com.irfan.moviecatalog.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.irfan.moviecatalog.data.remote.ApiService
import com.irfan.moviecatalog.data.remote.response.MovieDetailResponse
import com.irfan.moviecatalog.data.remote.response.ReviewResponse.ReviewItem
import com.irfan.moviecatalog.data.remote.response.VideoResponse
import com.irfan.moviecatalog.ui.detail.review.ReviewPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getDetailMovie(movieId: Int): Flow<MovieDetailResponse> = flow {
        emit(apiService.getDetailMovie(movieId))
    }.flowOn(Dispatchers.IO)

    fun getVideos(movieId: Int): Flow<VideoResponse> = flow {
        emit(apiService.getVideos(movieId))
    }.flowOn(Dispatchers.IO)

    fun getReviews(movieId: Int): Flow<PagingData<ReviewItem>> {
        return Pager(PagingConfig(pageSize = 10)) {
            ReviewPagingSource(apiService, movieId)
        }.flow
    }
}