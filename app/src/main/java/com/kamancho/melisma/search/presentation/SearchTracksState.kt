package com.kamancho.melisma.search.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kamancho.melisma.databinding.DefaultLoadStateBinding

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
