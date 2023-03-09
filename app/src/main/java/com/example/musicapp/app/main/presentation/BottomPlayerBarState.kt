package com.example.musicapp.app.main.presentation

import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.media3.common.MediaItem

/**
 * Created by HP on 31.01.2023.
 **/
interface BottomPlayerBarState {

    fun apply(
        imgBtn: ToggleButton,
        bottomPlayer: ConstraintLayout,
        trackName: TextView,
        authorName: TextView
    )

    abstract class Active(
        private val track: MediaItem
    ): BottomPlayerBarState{

        override fun apply(
            imgBtn: ToggleButton,
            bottomPlayer: ConstraintLayout,
            trackName: TextView,
            authorName: TextView
        ) {
            trackName.text = track.mediaMetadata.title
            authorName.text = track.mediaMetadata.artist
            bottomPlayer.visibility = VideoView.VISIBLE
        }

    }

    data class  Pause(
        private val track: MediaItem
        ): Active(track){

        override fun apply(
            imgBtn: ToggleButton,
            bottomPlayer: ConstraintLayout,
            trackName: TextView,
            authorName: TextView
        ) {
            if(bottomPlayer.visibility ==View.GONE) super.apply(imgBtn, bottomPlayer, trackName, authorName)
            imgBtn.isChecked = true
        }
    }

    data class Play(
        private val track: MediaItem
            ) : Active(track){
        override fun apply(imgBtn: ToggleButton,
                           bottomPlayer: ConstraintLayout,
                           trackName: TextView,
                           authorName: TextView) {
            super.apply(imgBtn, bottomPlayer, trackName, authorName)
            imgBtn.isChecked = false
        }
    }



    object Disabled: BottomPlayerBarState{

        override fun apply(
            imgBtn: ToggleButton,
            bottomPlayer: ConstraintLayout,
            trackName: TextView,
            authorName: TextView
        ) {
            bottomPlayer.visibility = View.GONE
        }

    }


}