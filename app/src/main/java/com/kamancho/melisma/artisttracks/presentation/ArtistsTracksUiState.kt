package com.kamancho.melisma.artisttracks.presentation

import android.view.View
import androidx.core.view.isVisible
import com.kamancho.melisma.R
import com.kamancho.melisma.databinding.ArtistTracksListFragmentBinding

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
interface ArtistsTracksUiState {

    fun apply(binging: ArtistTracksListFragmentBinding)

    object Success : ArtistsTracksUiState {
        override fun apply(binging: ArtistTracksListFragmentBinding) {
            binging.artistTracksRcv.visibility = View.VISIBLE
            binging.errorImg.visibility = View.GONE
            binging.errorTv.visibility = View.GONE
            binging.reloadBtn.visibility = View.GONE
            binging.artistTracksProgressBar.visibility = View.GONE
        }
    }

    data class Failure(
        private val message: String,
        private val reloadBtnVisibility: Boolean
    ) : ArtistsTracksUiState {
        override fun apply(binging: ArtistTracksListFragmentBinding) {
            binging.artistTracksProgressBar.visibility = View.GONE
            binging.errorImg.setImageResource(R.drawable.no_results)
            binging.errorImg.visibility = View.VISIBLE
            binging.errorTv.text = message
            binging.errorTv.visibility = View.VISIBLE
            binging.reloadBtn.isVisible = reloadBtnVisibility
            binging.artistTracksRcv.visibility = View.GONE
        }
    }

    object Loading: ArtistsTracksUiState {
        override fun apply(binging: ArtistTracksListFragmentBinding) {
            binging.errorImg.visibility = View.GONE
            binging.errorTv.visibility = View.GONE
            binging.reloadBtn.visibility = View.GONE
            binging.artistTracksRcv.visibility = View.GONE
            binging.artistTracksProgressBar.visibility = View.VISIBLE
        }
    }

    object Empty: ArtistsTracksUiState {
        override fun apply(binging: ArtistTracksListFragmentBinding) = Unit
    }
}