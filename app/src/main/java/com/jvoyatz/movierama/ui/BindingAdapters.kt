package com.jvoyatz.movierama.ui

import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jvoyatz.movierama.R
import com.jvoyatz.movierama.domain.models.Cast
import com.jvoyatz.movierama.domain.models.Crew

private const val TAG = "BindingAdapters"


@BindingAdapter("app:setDirector")
fun setDirectorText(view: TextView, cast: List<Crew>?){
    cast?.let{ it ->
        it.filter { it.job.lowercase() == "director" }
            ?.let { directors ->
                view.text = directors.joinToString { it.name }
            }
    }
}

@BindingAdapter("app:setCast")
fun setCastText(view: TextView, cast: List<Cast>?){
    cast?.let{ list ->
        list.joinToString { it.name }
            .also {
                view.text = it
            }
    }
}
@BindingAdapter("app:imgUrl")
fun setImage(view: ImageView, url: String?){
    url?.let {
        val context = view.context
        Glide.with(context)
            .load(url)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.ic_baseline_broken_image_24))
            .into(view)
    }
}

@BindingAdapter("app:rating")
fun setRating(view: RatingBar, rating: Double){
    rating?.let {
        view.rating = it.toFloat()
    }
}