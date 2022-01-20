package com.jvoyatz.movierama.ui.movies

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jvoyatz.movierama.R
import com.jvoyatz.movierama.databinding.FragmentMoviesBinding
import com.jvoyatz.movierama.ui.details.DetailsFragment

class MoviesFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.myLooper()!!)
            .postDelayed({
                 findNavController().navigate(R.id.action_moviesFragment_to_detailsFragment)
            }, 5000)
    }
}