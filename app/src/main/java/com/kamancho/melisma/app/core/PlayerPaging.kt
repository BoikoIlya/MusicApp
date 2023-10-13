package com.kamancho.melisma.app.core

import androidx.media3.common.MediaItem
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.trending.presentation.MediaControllerWrapper
import javax.inject.Inject

/**
 * Created by HP on 27.09.2023.
 **/
interface PlayerPaging {

    fun newPage(
        controller: MediaControllerWrapper,
        mediaItem: MediaItem
    ): List<MediaItem>

    fun newPlaylist(playlist: List<MediaItem>)

//    class Base @Inject constructor(
//        private val temporaryTracksCache: TemporaryTracksCache
//    ): PlayerPaging{
//
//        private var subPlaylists = mutableListOf<List<MediaItem>>()
//
//        companion object {
//            private const val pageSize = 30
//        }
//        override fun newPage(
//            controller: MediaControllerWrapper,
//            mediaItem: MediaItem,
//        ): List<MediaItem> {
//
//            val index = controller.currentMediaItemIndex
//            val subPlaylistIndex =  index/pageSize
//            val remains = index - (subPlaylistIndex* pageSize)
//            if(remains==0){
//                controller.addMediaItems(subPlaylists[subPlaylistIndex])
//            }
//
//        }
//
//        override fun newPlaylist(playlist: List<MediaItem>) {
//            playlist.chunked(pageSize){
//                subPlaylists.add(it)
//            }
//        }
//    }
}