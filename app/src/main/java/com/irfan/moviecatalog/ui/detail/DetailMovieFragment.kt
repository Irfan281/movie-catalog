package com.irfan.moviecatalog.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.irfan.moviecatalog.R
import com.irfan.moviecatalog.databinding.FragmentDetailMovieBinding
import com.irfan.moviecatalog.ui.detail.review.ReviewPagingAdapter
import com.irfan.moviecatalog.utils.ApiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMovieFragment : Fragment() {
    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailMovieViewModel by activityViewModels()
    private val args: DetailMovieFragmentArgs by navArgs()
    private lateinit var reviewAdapter: ReviewPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        observeData()
    }

    private fun initUI() {
        reviewAdapter = ReviewPagingAdapter(true)

        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = reviewAdapter
        }

        binding.apply {
            topAppBar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }

            reviewHeader.setOnClickListener {
                findNavController().navigate(
                    DetailMovieFragmentDirections.actionDetailMovieFragmentToReviewFragment(
                        args.id
                    )
                )
            }

            btnAllReviews.setOnClickListener {
                findNavController().navigate(
                    DetailMovieFragmentDirections.actionDetailMovieFragmentToReviewFragment(
                        args.id
                    )
                )
            }
        }
    }

    private fun observeData() {
        viewModel.setMovieId(args.id)

        //observe detail movie data
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.detailMovie.collect {
                when (it) {
                    is ApiState.Loading -> {}

                    is ApiState.Success -> {
                        val data = it.data

                        binding.apply {
                            mainPoster.load("https://image.tmdb.org/t/p/w400/${data.posterPath}") {
                                crossfade(true)
                                crossfade(500)
                                transformations(RoundedCornersTransformation(20f))
                            }
                            title.text = data.title
                            overview.text = data.overview
                            genre.text =
                                data.genres.joinToString(" | ") { genreItem -> genreItem.name }

                            nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                                val titleVisibilityThreshold =
                                    resources.getDimensionPixelSize(R.dimen.title_visibility_threshold)
                                if (scrollY >= titleVisibilityThreshold) {
                                    topAppBar.title = data.title
                                } else {
                                    topAppBar.title = ""
                                }
                            }
                        }

                    }

                    is ApiState.Error -> {}
                }
            }
        }

        //observe video data
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.video.collect {
                when (it) {
                    is ApiState.Loading -> {
                    }

                    is ApiState.Success -> {
                        val data = it.data

                        binding.rvVideos.apply {
                            layoutManager = LinearLayoutManager(
                                requireActivity(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            adapter = VideoAdapter(data.results)
                        }
                    }

                    is ApiState.Error -> {

                    }
                }
            }
        }

        //observe review data
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.review.collectLatest {
                reviewAdapter.submitData(it)

            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            reviewAdapter.loadStateFlow.collectLatest { loadState ->
                binding.apply {
                    tvNoReviews.isVisible = loadState.refresh is LoadState.Error
                    rvReviews.isVisible = loadState.refresh !is LoadState.Error

                    if (loadState.refresh is LoadState.Error) {
                        val msg = (loadState.refresh as LoadState.Error).error.message
                        if (msg == "No Data") {
                            btnAllReviews.isVisible = false
                            reviewHeader.isClickable = false
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}