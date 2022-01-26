package com.jvoyatz.movierama.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.databinding.FragmentDetailsBinding
import com.jvoyatz.movierama.domain.models.Movie
import com.jvoyatz.movierama.domain.models.MovieDetails
import com.jvoyatz.movierama.ui.movies.Handler
import com.jvoyatz.movierama.ui.setFavoriteImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DetailsFragment @Inject constructor(): Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var viewmodel: DetailsViewModel
    private val args: DetailsFragmentArgs by navArgs()


    var handler = object : DetailsHandler {
        override fun markMovieAsFavorite(movie: MovieDetails) {
            viewmodel.markMovieAsFavorite(movie.id, movie.title)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.movie = args.movie
        binding.handler = handler

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.favoriteMovieState.collectLatest {
                    if(it) {
                        Toast.makeText(
                            requireContext(),
                            "Movie marked as favorite",
                            Toast.LENGTH_SHORT
                        ).show()
                        setFavoriteImage(binding.movieDetailFavorite, true)
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