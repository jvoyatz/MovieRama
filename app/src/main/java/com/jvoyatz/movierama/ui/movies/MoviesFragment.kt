package com.jvoyatz.movierama.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jvoyatz.movierama.common.Resource
import com.jvoyatz.movierama.databinding.FragmentMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "MoviesFragment"

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private lateinit var moviesViewModel: MoviesViewModel

    private lateinit var binding: FragmentMoviesBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        binding.moviesRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        MoviesAdapter(CoroutineScope(Dispatchers.Main)){
            it?.let {
                moviesViewModel.getMovieDetails(it.id)
            }
            }.also {
                binding.moviesRecyclerview.adapter = it
            }

        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

        lifecycleScope.launch {
            binding.searchview
                .getOnQueryTextChangeListenerFlow()
                .debounce(250) //delay so as to avoid making unnecessary calls
//                .filter {  query ->
//                    return@filter query.isNotEmpty()
//                }
                .distinctUntilChanged() //avoid repetitions
                .flowOn(Dispatchers.Default)
                .collect {
                    moviesViewModel.searchForMovies(it)
                }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                moviesViewModel.moviesState.collect {
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



fun SearchView.getOnQueryTextChangeListenerFlow(): StateFlow<String> {
    val query = MutableStateFlow("")
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        override fun onQueryTextChange(newText: String): Boolean {
            query.value = newText
            return true
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

    })
    return query
}