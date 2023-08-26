package com.irfan.moviecatalog.ui.detail.review

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.irfan.moviecatalog.data.remote.ApiService
import com.irfan.moviecatalog.data.remote.response.ReviewResponse.ReviewItem

class ReviewPagingSource(
    private val service: ApiService,
    private val movieId: Int
) : PagingSource<Int, ReviewItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewItem> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = service.getReviewMovie(movieId, nextPageNumber)
            if (response.totalResults == 0) {
                return LoadResult.Error(Exception("No Data"))
            }
            LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = if (nextPageNumber == response.totalPages) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ReviewItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}