package com.jvoyatz.movierama.ui.movies

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jvoyatz.movierama.R
import com.jvoyatz.movierama.databinding.FragmentMoviesItemBinding
import com.jvoyatz.movierama.databinding.HeaderItemBinding
import com.jvoyatz.movierama.domain.models.Movie
import com.jvoyatz.movierama.ui.*
import kotlinx.coroutines.*

private const val TAG = "MoviesAdapter"
class MoviesAdapter(
    val scope: CoroutineScope,
    val clickListener: (Movie) -> Unit): ListAdapter<MovieItem, RecyclerView.ViewHolder>(MovieDiffCallback()){

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when(item){
            is MovieItem.Header -> HEADER
            is MovieItem.Data -> ITEM
            is MovieItem.Loading -> LOADING
            is MovieItem.Exhausted -> EXHAUSTED
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
                val binding: FragmentMoviesItemBinding =
                    FragmentMoviesItemBinding.inflate(inflater, parent, false)
                MovieViewHolder(binding = binding){
                    when (val dataItem = getItem(it)){
                        is MovieItem.Data -> clickListener(dataItem.data)
                        else -> {} //do nothing
                    }
                }
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
        if(holder is MovieViewHolder){
            holder.bind((getItem(position) as MovieItem.Data).data)
        }
    }

    fun submitLoading(){
        submitList(listOf(MovieItem.Loading))
    }

    fun submitLoadingMore() {
        scope.launch {
            val mutableCurrentList = currentList.toMutableList()
            mutableCurrentList.removeIf { it.id == LOADING }
            val items: List<MovieItem> =
                mutableCurrentList + listOf(MovieItem.Loading)

            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }

    fun submit(list: List<Movie> = listOf()){
        scope.launch {
            delay(250)
            val mutableCurrentList = currentList.toMutableList()
            mutableCurrentList.removeIf { it.id == LOADING || it.id == HEADER }

            val items: List<MovieItem> = when {
                list.isNullOrEmpty() -> listOf(MovieItem.Exhausted)
                list.isNotEmpty() -> {
                  listOf(MovieItem.Header) +  mutableCurrentList + list.map { MovieItem.Data(it) }
                }
                else -> { listOf<MovieItem>()}
            }

            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }

//    fun submit(){
//        scope.launch {
//            val mutableCurrentList = currentList.toMutableList()
//            mutableCurrentList.removeIf { it.id == LOADING }
//
//            val items: List<MovieItem> = when {
//                mutableCurrentList.isEmpty() -> listOf(MovieItem.Exhausted)
//                else -> {
//                    //  val mutableCurrentList = currentList.toMutableList()
//                    //  mutableCurrentList.removeIf { it.id == LOADING }
//                    listOf(MovieItem.Header) /*+ mutableCurrentList +*/ + list.map { MovieItem.Data(it)
//                    }
//                }
//            }
//            withContext(Dispatchers.Main){
//                submitList(items)
//            }
//        }
//    }
}

class MovieViewHolder(val binding:FragmentMoviesItemBinding, clickPosition: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root){
    init {
        itemView.setOnClickListener {
            clickPosition(adapterPosition)
        }
    }
    fun bind(item: Movie){
        binding.apply {
            this.movie = item
            executePendingBindings()
        }
    }
}

class MovieDiffCallback: ItemCallback<MovieItem>(){
    override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
        if(oldItem is MovieItem.Data && newItem is MovieItem.Data){
            return oldItem.data == newItem.data
        }
        return oldItem == newItem
    }
}

sealed class MovieItem {
    abstract val id: Int

    data class Data(val data: Movie): MovieItem(){
        override val id = data.id
    }
    object Loading: MovieItem(){
        override val id = LOADING
    }
    object Header: MovieItem(){
        override val id = HEADER
    }
    object Exhausted: MovieItem(){
        override val id = EXHAUSTED
    }
}