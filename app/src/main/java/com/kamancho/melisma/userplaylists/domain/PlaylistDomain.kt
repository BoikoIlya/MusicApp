package com.kamancho.melisma.userplaylists.domain

import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import com.kamancho.melisma.userplaylists.presentation.PlaylistThumbsState
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import java.time.Instant
import javax.inject.Inject

/**
 * Created by HP on 12.07.2023.
 **/
data class PlaylistDomain(
    private val playlistId: String,
    private val title: String,
    private val isFollowing: Boolean,
    private val count: Int,
    private val description: String,
    private val ownerId: Int,
    private val thumbs: List<String>
){

    fun <T>map(mapper: Mapper<T>): T = mapper.map(playlistId, title, isFollowing, count, description, ownerId, thumbs)

    interface Mapper<T>{

        fun map(
            playlistId: String,
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
            playlistId: String,
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
                update_time = System.currentTimeMillis().toInt(),
                description = description,
                owner_id = owner_id,
                thumbs = thumbs
            )
        }

    }

    class ToIdsMapper: Mapper<Pair<Int,String>>{

        override fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbs: List<String>,
        ): Pair<Int,String> {
            return Pair(owner_id,playlistId)
        }

    }

    class ToIdsMapperInt @Inject constructor(): Mapper<Pair<Int,Int>>{

        override fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbs: List<String>,
        ): Pair<Int,Int> {
            return Pair(owner_id,playlistId.toInt())
        }

    }

    class ToContainsMapper: Mapper<Pair<String,String>>{

        override fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbs: List<String>
        ): Pair<String, String> = Pair(title,"")
    }



    class ToPlaylistUi @Inject constructor(
        private val managerResource: ManagerResource
    ): Mapper<PlaylistUi>{

        companion object{
            private const val amount_of_images = 4
        }

        override fun map(
            playlistId: String,
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
            if(thumbs.isEmpty()) listOfStates[0] = PlaylistThumbsState.LoadImages("")

            return PlaylistUi(
                playlistId = playlistId,
                title = title,
                isFollowing = is_following,
                count = count,
                description = if(description.isEmpty()) managerResource.getString(R.string.no_description) else description,
                ownerId = owner_id,
                thumbStates = listOfStates
            )
        }
    }

    class ToIdMapper @Inject constructor(): Mapper<String>{
        override fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbs: List<String>,
        ): String {
            return playlistId
        }

    }

    class TitleIsEmpty @Inject constructor(): Mapper<Boolean> {
        override fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbs: List<String>,
        ): Boolean {
            return title.isEmpty()
        }
    }

    interface ToTitleMapper:Mapper<String> {

        class Base @Inject constructor() : ToTitleMapper  {
            override fun map(
                playlistId: String,
                title: String,
                is_following: Boolean,
                count: Int,
                description: String,
                owner_id: Int,
                thumbs: List<String>,
            ): String {
                return title
            }


        }
    }


}
