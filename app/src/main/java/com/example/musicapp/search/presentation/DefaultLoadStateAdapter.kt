package com.example.musicapp.search.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.databinding.DefaultLoadStateBinding

/**
 * Created by HP on 02.05.2023.
 **/
 interface OnRetry{
    fun onRetry()
}

class DefaultLoadStateAdapter(
    private val refresh: OnRetry,
    private val errorMessage: () -> String
): LoadStateAdapter<LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
       holder.bind(loadState,errorMessage,null)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): LoadStateViewHolder = LoadStateViewHolder(
           DefaultLoadStateBinding.inflate(
               LayoutInflater.from(parent.context),
                parent,
                false
       ), refresh, this)

}


class LoadStateViewHolder(
    private val binding: DefaultLoadStateBinding,
    private val refresh: OnRetry,
    private val adapter: DefaultLoadStateAdapter
): RecyclerView.ViewHolder(binding.root){

    init {
        binding.loadStateErrorBtn.setOnClickListener { refresh.onRetry() }
    }

    fun bind(loadState: LoadState, errorMessage:()-> String,error: Exception?) = with(binding){

        loadStateErrorTv.text = errorMessage.invoke()
        loadStateErrorTv.isVisible = loadState is LoadState.Error
        loadStateErrorBtn.isVisible =
            if(error!=null && adapter.itemCount==0) error !is NoSuchElementException
            else loadState is LoadState.Error
        loadStateProgress.isVisible = loadState is LoadState.Loading
    }

}

