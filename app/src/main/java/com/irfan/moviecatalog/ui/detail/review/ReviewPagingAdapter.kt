package com.irfan.moviecatalog.ui.detail.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.irfan.moviecatalog.R
import com.irfan.moviecatalog.data.remote.response.ReviewResponse.ReviewItem
import com.irfan.moviecatalog.databinding.ItemReviewBinding
import com.irfan.moviecatalog.utils.TextUtil.dateConverter

class ReviewPagingAdapter(private val limitItem: Boolean) :
    PagingDataAdapter<ReviewItem, ReviewPagingAdapter.ReviewViewHolder>(ReviewDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReviewItem) {
            binding.apply {
                userAvatar.load("https://secure.gravatar.com/avatar/" + item.authorDetails.avatarPath) {
                    crossfade(true)
                    crossfade(500)
                    transformations(RoundedCornersTransformation(10f))
                }
                userName.text = item.authorDetails.username
                userReviewDate.text = binding.root.context.getString(
                    R.string.star_rate,
                    item.authorDetails.rating.toString(),
                    dateConverter(item.createdAt)
                )
                userReview.text = item.content
            }
        }
    }

    override fun getItemCount(): Int {
        return if (limitItem) {
            super.getItemCount().coerceAtMost(3)
        } else {
            super.getItemCount()
        }
    }

}

class ReviewDiffCallback : DiffUtil.ItemCallback<ReviewItem>() {
    override fun areItemsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
        return oldItem == newItem
    }
}