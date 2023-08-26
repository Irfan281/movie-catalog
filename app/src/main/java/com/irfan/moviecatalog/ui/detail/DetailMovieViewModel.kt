package com.irfan.moviecatalog.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.irfan.moviecatalog.data.remote.response.MovieDetailResponse
import com.irfan.moviecatalog.data.remote.response.ReviewResponse.ReviewItem
import com.irfan.moviecatalog.data.remote.response.VideoResponse
import com.irfan.moviecatalog.data.repository.DetailRepository
import com.irfan.moviecatalog.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    private val detailRepository: DetailRepository
) : ViewModel() {
    private val _movieId = MutableStateFlow<Int?>(null)

    val detailMovie: Flow<ApiState<MovieDetailResponse>> = _movieId.flatMapLatest { id ->
        detailRepository.getDetailMovie(id ?: return@flatMapLatest emptyFlow())
            .map { ApiState.Success(it) }
            .catch { ApiState.Error(it.message.toString()) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ApiState.Loading)


    val video: Flow<ApiState<VideoResponse>> = _movieId.flatMapLatest { id ->
        detailRepository.getVideos(id ?: return@flatMapLatest emptyFlow())
            .map { ApiState.Success(it) }
            .catch { ApiState.Error(it.message.toString()) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ApiState.Loading)

    val review: Flow<PagingData<ReviewItem>> = _movieId.flatMapLatest {
        detailRepository.getReviews(it ?: return@flatMapLatest emptyFlow())
    }.cachedIn(viewModelScope)

    fun setMovieId(id: Int) {
        _movieId.value = id
    }
}