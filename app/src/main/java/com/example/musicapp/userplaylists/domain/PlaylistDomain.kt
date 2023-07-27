package com.example.musicapp.userplaylists.domain

import com.example.musicapp.R
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.presentation.PlaylistThumbsState
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import java.time.Instant
import javax.inject.Inject

/**
 * Created by HP on 12.07.2023.
 **/
data class PlaylistDomain(
   private val playlistId: Int,
   private val title: String,
   private val is_following: Boolean,
   private val count: Int,
   private val description: String,
   private val owner_id: Int,
   private val thumbs: List<String>
){

    fun <T>map(mapper: Mapper<T>): T = mapper.map(playlistId, title, is_following, count, description, owner_id, thumbs)

    interface Mapper<T>{

        fun map(
            playlistId: Int,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbs: List<String>
        ): T
    }


    class ToPlaylistCacheMapper @Inject constructor(): Mapper<PlaylistCache>{

        override fun map(
            playlistId: Int,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbs: List<String>,
        ): PlaylistCache {
            return PlaylistCache(
                playlistId = playlistId,
                title = title,
                is_following = is_following,
                count = count,
                create_time = Instant.now().toEpochMilli().toInt(),
                description = description,
                owner_id = owner_id,
                thumbs = thumbs
            )
        }

    }

    class ToIdsMapper @Inject constructor(): Mapper<Pair<Int,Int>>{

        override fun map(
            playlistId: Int,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbs: List<String>,
        ): Pair<Int,Int> {
            return Pair(owner_id,playlistId)
        }

    }

    class ToContainsMapper @Inject constructor(): Mapper<Pair<String,String>>{

        override fun map(
            playlistId: Int,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbs: List<String>
        ): Pair<String, String> = Pair(title,"")
    }

    class ToPlaylistUi @Inject constructor(): Mapper<PlaylistUi>{

        companion object{
            private const val amount_of_images = 4
        }

        override fun map(
            playlistId: Int,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbs: List<String>
        ): PlaylistUi {

                val listOfStates = emptyList<PlaylistThumbsState>().toMutableList()
                var i = 0
                while (i < amount_of_images) {
                    listOfStates.add(
                        if (thumbs.size > i) PlaylistThumbsState.LoadImages(thumbs[i])
                        else PlaylistThumbsState.Empty
                    )
                    i++
                }
            if(thumbs.isEmpty()) listOfStates[0] = PlaylistThumbsState.LoadImages(""+ R.drawable.im)

            return PlaylistUi(
                playlistId = playlistId,
                title = title,
                is_following = is_following,
                count = count,
                description = description,
                owner_id = owner_id,
                thumbStates = listOfStates
            )
        }
    }


}
