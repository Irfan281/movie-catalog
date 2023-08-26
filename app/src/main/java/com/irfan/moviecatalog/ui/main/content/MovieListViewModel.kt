package com.irfan.moviecatalog.ui.main.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.irfan.moviecatalog.data.repository.MovieRepository
import com.irfan.moviecatalog.utils.MovieEndpoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val nowPlaying =
        movieRepository.getMoviesByEndpoint(MovieEndpoint.NOW_PLAYING).cachedIn(viewModelScope)
    val upcoming =
        movieRepository.getMoviesByEndpoint(MovieEndpoint.UPCOMING).cachedIn(viewModelScope)
    val popular =
        movieRepository.getMoviesByEndpoint(MovieEndpoint.POPULAR).cachedIn(viewModelScope)
    val topRated =
        movieRepository.getMoviesByEndpoint(MovieEndpoint.TOP_RATED).cachedIn(viewModelScope)
}