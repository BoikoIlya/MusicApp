package com.example.musicapp.favoritesplaylistdetails.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.R
import com.example.musicapp.userplaylists.presentation.PlaylistUi

/**
 * Created by HP on 04.08.2023.
 **/

sealed interface PlaylistDetailsUi {

  //  fun bind(viewHolder: AdapterViewHolder,position: Int,selectedPosition: Int)

    fun viewType(): Int

//    fun compareId(item: PlaylistDetailsUi): Boolean


    data class BaseMediaItem(
        private val mediaItem: MediaItem
    ): PlaylistDetailsUi {

//        override fun bind(viewHolder: AdapterViewHolder,position: Int,selectedPosition: Int)
//        = viewHolder.bind(mediaItem,position,selectedPosition)
        override fun viewType(): Int = R.layout.track_item

//        override fun compareId(item: PlaylistDetailsUi): Boolean = (item as BaseMediaItem).mediaItem.mediaId ==mediaItem.mediaId


    }

    data class BasePlaylistDetailsTopBar(
        private val playlistUi: PlaylistUi
    ):PlaylistDetailsUi {

//        override fun bind(viewHolder: AdapterViewHolder,position: Int,selectedPosition: Int)
//        = viewHolder.bind(playlistUi)
        override fun viewType(): Int = R.layout.playlist_details_topbar


//        override fun compareId(item: PlaylistDetailsUi): Boolean = (item as BasePlaylistDetailsTopBar)


    }
}
