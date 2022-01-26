package com.jvoyatz.movierama.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.jvoyatz.movierama.R
import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DetailsFragment @Inject constructor(): Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var viewmodel: DetailsViewModel
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.movie = args.movie

        binding.movieDetailReviewsList.layoutManager = LinearLayoutManager(requireActivity())
        var movieReviewsAdapter = MovieReviewsAdapter(CoroutineScope(Dispatchers.Main))
        binding.movieDetailReviewsList.adapter = movieReviewsAdapter
        var similarMoviesAdapter = SimilarMoviesAdapter(CoroutineScope(Dispatchers.Main))
        binding.movieDetailSimilarList.adapter = similarMoviesAdapter

        viewmodel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        NavigationUI.setupWithNavController(binding.toolbar, navController)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.similarMoviesState.collect {
                    val adapter =
                        binding.movieDetailSimilarList.adapter as SimilarMoviesAdapter
                    when(it){
                        is Resource.Success -> {
                            adapter.submit(it.data.similarMovies)
                        }
                        is Resource.Loading -> {
                            adapter.submitLoading()
                        }
                        is Resource.Error -> {
                            adapter.submit(listOf())
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.reviewsState.collect {
                    val adapter =
                        binding.movieDetailReviewsList.adapter as MovieReviewsAdapter
                    when(it){
                        is Resource.Success -> {
                            adapter.submit(it.data.movieReview)
                        }
                        is Resource.Loading -> {
                            adapter.submitLoading()
                        }
                        is Resource.Error -> {
                            adapter.submit(listOf())
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}