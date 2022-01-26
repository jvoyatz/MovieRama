package com.jvoyatz.movierama.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jvoyatz.movierama.R
import com.jvoyatz.movierama.databinding.FragmentSimilarMoviesItemBinding
import com.jvoyatz.movierama.databinding.HeaderItemBinding
import com.jvoyatz.movierama.domain.models.SimilarMovie
import com.jvoyatz.movierama.ui.*
import kotlinx.coroutines.*

class SimilarMoviesAdapter(val scope: CoroutineScope): ListAdapter<SimilarMovieDataItem, ViewHolder>(SimilarMovieDiffCallback()){

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when(item){
            is SimilarMovieDataItem.Header -> HEADER
            is SimilarMovieDataItem.Data -> ITEM
            is SimilarMovieDataItem.Loading -> LOADING
            is SimilarMovieDataItem.Exhausted -> EXHAUSTED
            else -> {
                throw IllegalStateException("exception")
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            HEADER -> {
                BaseViewHolder.create<HeaderItemBinding>(parent, R.layout.header_item)
            }
            ITEM -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding: FragmentSimilarMoviesItemBinding =
                    FragmentSimilarMoviesItemBinding.inflate(inflater, parent, false)
                    SimilarMovieViewHolder(binding = binding)
            }
            EXHAUSTED -> {
                BaseViewHolder.create<HeaderItemBinding>(parent, R.layout.exhausted_item)
            }
            LOADING -> {
                BaseViewHolder.create<HeaderItemBinding>(parent, R.layout.loading_item)
            }
            else -> {
                throw java.lang.IllegalStateException("error in adapter")
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(holder is SimilarMovieViewHolder){
            holder.binding.movie = (getItem(position) as SimilarMovieDataItem.Data).data
        }
    }

    fun submitLoading(){
        submitList(listOf(SimilarMovieDataItem.Loading))
    }

    fun submit(list: List<SimilarMovie>){
        scope.launch {
            delay(450)
            val items: List<SimilarMovieDataItem> = when {
                list.isNullOrEmpty() -> listOf(SimilarMovieDataItem.Exhausted)
                else -> list.map { SimilarMovieDataItem.Data(it) }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }
}

class SimilarMovieViewHolder(val binding:FragmentSimilarMoviesItemBinding) : ViewHolder(binding.root){
    fun bind(item: SimilarMovie){
        binding.apply {
           this.movie = item
            executePendingBindings()
        }
    }
}

class SimilarMovieDiffCallback: ItemCallback<SimilarMovieDataItem>(){
    override fun areItemsTheSame(oldItem: SimilarMovieDataItem, newItem: SimilarMovieDataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SimilarMovieDataItem, newItem: SimilarMovieDataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class SimilarMovieDataItem {
    abstract val id: Int

    data class Data(val data: SimilarMovie): SimilarMovieDataItem(){
        override val id = data.id
    }
    object Loading: SimilarMovieDataItem(){
        override val id = LOADING
    }
    object Header: SimilarMovieDataItem(){
        override val id = HEADER
    }
    object Exhausted: SimilarMovieDataItem(){
        override val id = EXHAUSTED
    }
}