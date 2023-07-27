package com.example.musicapp.creteplaylist.presentation

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import com.example.musicapp.databinding.PlaylistDataFragmentBinding

/**
 * Created by HP on 17.07.2023.
 **/
sealed interface PlaylistDataSaveBtnUiState{

    fun apply(
        binding: PlaylistDataFragmentBinding,
        context: Context
    )

    object Hide: PlaylistDataSaveBtnUiState {


        override fun apply(
            binding: PlaylistDataFragmentBinding,
            context: Context
        ) = with(binding){
            if(!saveBtn.isVisible) return@with
            val animation = AnimationUtils.loadAnimation(context, com.airbnb.lottie.R.anim.abc_fade_out)
            saveBtn.startAnimation(animation)
            animation.duration = 200
            saveBtn.visibility = View.GONE
        }
    }

    object Show: PlaylistDataSaveBtnUiState{

        override fun apply(
            binding: PlaylistDataFragmentBinding,
            context: Context
        ) = with(binding){
            val animation = AnimationUtils.loadAnimation(context, com.airbnb.lottie.R.anim.abc_fade_in)
            animation.duration = 200
            saveBtn.startAnimation(animation)
            saveBtn.visibility = View.VISIBLE
        }

    }

    object Empty: PlaylistDataSaveBtnUiState{
        override fun apply(binding: PlaylistDataFragmentBinding, context: Context) = Unit
    }
}