package com.jvoyatz.movierama.ui.details

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jvoyatz.movierama.R
import com.jvoyatz.movierama.databinding.FragmentMoviesItemBinding
import com.jvoyatz.movierama.databinding.FragmentSimilarMoviesItemBinding
import com.jvoyatz.movierama.databinding.FragmentSimilarReviewsItemBinding
import com.jvoyatz.movierama.databinding.HeaderItemBinding
import com.jvoyatz.movierama.domain.models.MovieReview
import com.jvoyatz.movierama.domain.models.SimilarMovie
import com.jvoyatz.movierama.ui.*
import kotlinx.coroutines.*

private const val TAG = "MovieReviewsAdapter"
class MovieReviewsAdapter(val scope: CoroutineScope): ListAdapter<MovieReviewDataItem, ViewHolder>(MovieReviewItemCallback()){

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when(item){
            is MovieReviewDataItem.Data -> ITEM
            is MovieReviewDataItem.Loading -> LOADING
            is MovieReviewDataItem.Exhausted -> EXHAUSTED
            else -> {
                throw IllegalStateException("exception")
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            ITEM -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding: FragmentSimilarReviewsItemBinding =
                    FragmentSimilarReviewsItemBinding.inflate(inflater, parent, false)
                MovieReviewViewHolder(binding = binding)
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
        if(holder is MovieReviewViewHolder){
            holder.bind((getItem(position) as MovieReviewDataItem.Data).data)
        }
    }

    fun submitLoading(){
        submitList(listOf(MovieReviewDataItem.Loading))
    }

    fun submit(list: List<MovieReview>){
        scope.launch {
            delay(450)
            val items: List<MovieReviewDataItem> = when {
                list.isNullOrEmpty() -> listOf(MovieReviewDataItem.Exhausted)
                else -> list.map { MovieReviewDataItem.Data(it) }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }
}

class MovieReviewViewHolder(val binding:FragmentSimilarReviewsItemBinding) : ViewHolder(binding.root){
    fun bind(item: MovieReview){
        Log.d(TAG, "bind() called with: item = $item")
        binding.apply {
            this.review = item
            executePendingBindings()
        }
    }
}

class MovieReviewItemCallback: ItemCallback<MovieReviewDataItem>(){
    override fun areItemsTheSame(oldItem: MovieReviewDataItem, newItem: MovieReviewDataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieReviewDataItem, newItem: MovieReviewDataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class MovieReviewDataItem {
    abstract val id: String

    data class Data(val data: MovieReview): MovieReviewDataItem(){
        override val id = data.id
    }
    object Loading: MovieReviewDataItem(){
        override val id = LOADING.toString()
    }
    object Header: MovieReviewDataItem(){
        override val id = HEADER.toString()
    }
    object Exhausted: MovieReviewDataItem(){
        override val id = EXHAUSTED.toString()
    }
}