package com.example.musicapp.search.presentation

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.databinding.DefaultLoadStateBinding

/**
 * Created by HP on 02.05.2023.
 **/
sealed interface SearchTracksState {

    fun apply(
        rcv: RecyclerView,
        binding: DefaultLoadStateBinding,
    )

    object Success : SearchTracksState {

        override fun apply(
            rcv: RecyclerView,
            binding: DefaultLoadStateBinding
        ) {
            binding.loadStateErrorBtn.visibility = View.VISIBLE
        }

    }



    object Error: SearchTracksState {

        override fun apply(
            rcv: RecyclerView,
            binding: DefaultLoadStateBinding
        ) {
            rcv.visibility = View.GONE
        }

    }


    object Empty : SearchTracksState {
        override fun apply(
            rcv: RecyclerView,
            binding: DefaultLoadStateBinding
        ) = Unit
    }
}
