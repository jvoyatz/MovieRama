package com.jvoyatz.movierama.ui.movies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.databinding.FragmentMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MoviesFragment"

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private lateinit var moviesViewModel: MoviesViewModel

    private lateinit var binding: FragmentMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)

        binding.moviesRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        val adapter = MoviesAdapter(CoroutineScope(Dispatchers.Main)){
            it?.let {
                moviesViewModel.getMovieDetails(it.id)
            }
        }.also {
            binding.moviesRecyclerview.adapter = it
        }
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                moviesViewModel.popularMoviesState.collectLatest {
                    it?.let {
                        var moviesAdapter = binding.moviesRecyclerview.adapter as MoviesAdapter

                        when(it){
                            is Resource.Success -> {
                                moviesAdapter.submit(it.data.results)
                            }
                            is Resource.Loading -> {
                                moviesAdapter.submitLoading()
                            }
                            is Resource.Error -> {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                moviesViewModel.movieDetailsState.collectLatest {
                    it?.let {
                        when(it){
                            is Resource.Success -> {
                                findNavController().navigate(MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment(it.data))
                            }
                            is Resource.Loading -> {

                            }
                            is Resource.Error -> {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}