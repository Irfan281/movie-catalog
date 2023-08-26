package com.irfan.moviecatalog.ui.main.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.irfan.moviecatalog.databinding.FragmentContentBinding
import com.irfan.moviecatalog.utils.LoadingStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class ContentFragment : Fragment() {

    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding!!

    private var position by Delegates.notNull<Int>()

    private val viewModel by viewModels<MovieListViewModel>()

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        observeData()
    }

    private fun initUI() {
        arguments?.let {
            position = it.getInt("number")
        }

        movieAdapter = MovieAdapter()
        binding.rvMovieList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = movieAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    movieAdapter.retry()
                }
            )
        }

        binding.refresh.apply {
            setOnRefreshListener {
                movieAdapter.refresh()
                isRefreshing = false
            }
        }

        binding.btnRetry.setOnClickListener {
            movieAdapter.retry()
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            when (position) {
                1 -> {
                    viewModel.nowPlaying.collectLatest {
                        movieAdapter.submitData(it)
                    }
                }

                2 -> {
                    viewModel.popular.collectLatest {
                        movieAdapter.submitData(it)
                    }
                }

                3 -> {
                    viewModel.topRated.collectLatest {
                        movieAdapter.submitData(it)
                    }
                }

                4 -> {
                    viewModel.upcoming.collectLatest {
                        movieAdapter.submitData(it)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            movieAdapter.loadStateFlow.collectLatest { loadState ->
                binding.apply {
                    progressBar.isVisible = loadState.refresh is LoadState.Loading
                    btnRetry.isVisible = loadState.refresh is LoadState.Error
                    tvErrorMsg.isVisible = loadState.refresh is LoadState.Error
                    rvMovieList.isVisible = loadState.refresh !is LoadState.Error
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}