package com.irfan.moviecatalog.ui.main.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.irfan.moviecatalog.R
import com.irfan.moviecatalog.data.remote.response.MovieItem
import com.irfan.moviecatalog.databinding.ItemMovieBinding
import com.irfan.moviecatalog.ui.main.MainFragmentDirections

class MovieAdapter() :
    PagingDataAdapter<MovieItem, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieItem) {
            binding.apply {
                moviePoster.load("https://image.tmdb.org/t/p/w500${item.posterPath}") {
                    crossfade(true)
                    crossfade(1000)
                    transformations(RoundedCornersTransformation(20f))
                }
                movieTitle.text = item.title
                movieRate.text = binding.root.context.getString(
                    R.string.rating,
                    item.voteAverage.toString(),
                    item.voteCount.toString()
                )
            }

            binding.root.apply {
                setOnClickListener {
                    findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToDetailMovieFragment(item.id)
                    )
                }
            }
        }
    }


}

class MovieDiffCallback : DiffUtil.ItemCallback<MovieItem>() {
    override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
        return oldItem == newItem
    }
}