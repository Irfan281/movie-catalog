package com.irfan.moviecatalog.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.irfan.moviecatalog.data.remote.response.VideoResponse
import com.irfan.moviecatalog.databinding.ItemVideoBinding
import com.irfan.moviecatalog.utils.TextUtil.generateYoutubeThumbnail

class VideoAdapter(private val videos: List<VideoResponse.VideoItem>) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    inner class ViewHolder(private var binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoResponse.VideoItem) {
            if (item.site.equals("youtube", true)) {
                binding.apply {
                    videoThumbnail.load(generateYoutubeThumbnail(item.key)) {
                        crossfade(true)
                        crossfade(300)
                        transformations(RoundedCornersTransformation(20f))
                    }
                    videoTitle.text = item.name
                }

                binding.root.setOnClickListener {
                    it.findNavController().navigate(
                        DetailMovieFragmentDirections.actionDetailMovieFragmentToVideoFragment2(item.key)
                    )
                }
            }
        }
    }
}