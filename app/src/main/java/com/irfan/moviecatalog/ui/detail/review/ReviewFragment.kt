package com.irfan.moviecatalog.ui.detail.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.irfan.moviecatalog.databinding.FragmentReviewBinding
import com.irfan.moviecatalog.ui.detail.DetailMovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewFragment : Fragment() {
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailMovieViewModel by activityViewModels()
    private lateinit var reviewAdapter: ReviewPagingAdapter
    private val args: ReviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        observeData()
    }

    private fun initUI() {
        reviewAdapter = ReviewPagingAdapter(false)

        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = reviewAdapter
        }

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun observeData() {
        viewModel.setMovieId(args.id)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.review.collectLatest {
                reviewAdapter.submitData(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}