package com.jvoyatz.movierama.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.R
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private var isLoading = false;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

        //binding
        binding = FragmentMoviesBinding.inflate(inflater, container, false)

        //recyclerview
        binding.moviesRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.moviesRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!isLoading && !recyclerView.canScrollVertically(1)){
                    val moviesAdapter = binding.moviesRecyclerview.adapter as MoviesAdapter
                    moviesAdapter.submitLoadingMore()
                    isLoading = true
                    moviesViewModel.getMoviesNextPage()
                }
            }
        })
        MoviesAdapter(CoroutineScope(Dispatchers.Main)){
            it?.let {
                moviesViewModel.getMovieDetails(it.id)
            }
            }.also {
                binding.moviesRecyclerview.adapter = it
            }
        //searchview
        val clearButton: ImageView = binding.searchview.findViewById(R.id.search_close_btn)
        clearButton.setOnClickListener {
            binding.searchview.setQuery("", false)
            moviesViewModel.setSearchMoviesQuery(null)
        }

        lifecycleScope.launch {
            binding.searchview
                .getOnQueryTextChangeListenerFlow()
                .debounce(350) //delay so as to avoid making unnecessary calls
//                .filter {  query ->
//                    return@filter query.isNotEmpty()
//                }
                .distinctUntilChanged() //avoid repetitions
                .flowOn(Dispatchers.Default)
                .collect {
                    val moviesAdapter = binding.moviesRecyclerview.adapter as MoviesAdapter
                    moviesAdapter.submitLoading()
                    moviesViewModel.setSearchMoviesQuery(it)
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
                        val moviesAdapter = binding.moviesRecyclerview.adapter as MoviesAdapter

                        when(it){
                            is Resource.Success -> {
                                moviesAdapter.submit(it.data.results)
                                isLoading = false
                            }
                            is Resource.Loading -> {
                                moviesAdapter.submitLoading()
                                isLoading = true
                            }
                            is Resource.Error -> {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                                moviesAdapter.submit()
                            }
                            else -> {}
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