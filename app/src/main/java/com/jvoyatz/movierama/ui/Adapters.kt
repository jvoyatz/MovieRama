package com.jvoyatz.movierama.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


const val LOADING = 1
const val ITEM = 2
const val HEADER = 3
const val EXHAUSTED = 4


class BaseViewHolder<T : ViewDataBinding>(binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root){
    companion object{
        fun <T : ViewDataBinding> create(parent: ViewGroup, layoutId: Int):BaseViewHolder<T>{
            val inflater = LayoutInflater.from(parent.context)
            var binding: T = DataBindingUtil.inflate(inflater, layoutId, parent, false)
            return BaseViewHolder<T>(binding = binding)
        }
    }
}

interface BaseClickListener<T>{
    fun onClick(item: T);
}
