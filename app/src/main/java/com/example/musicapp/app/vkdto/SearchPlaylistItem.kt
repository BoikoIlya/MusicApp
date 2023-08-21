package com.example.musicapp.app.vkdto

import com.example.musicapp.R
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.PlaylistThumbsState
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 14.08.2023.
 **/
data class SearchPlaylistItem(
   private val id: Int,
   private val owner_id: Int,
   private val type: Int,
   private val title: String,
   private val description: String,
   private val count: Int,
   private val followers: Int,
   private val plays: Int,
   private val create_time: Int,
   private val update_time: Int,
   private val genres: List<Any>,
   private val is_following: Boolean,
   private val photo: Photo?=null,
   private val permissions: Any,
   private val subtitle_badge: Boolean,
   private val play_button: Boolean,
   private val access_key: String,
   private val album_type: String,
   private val exclusive: Boolean
){

    companion object{
        private const val amount_of_images_need = 4
    }

    fun <T>map(mapper: Mapper<T>):T = mapper.map(
        id,
        owner_id,
        type,
        title,
        description,
        count,
        followers,
        plays,
        create_time,
        update_time,
        genres,
        is_following,
        photo,
        permissions,
        subtitle_badge,
        play_button,
        access_key,
        album_type,
        exclusive
    )

    interface Mapper<T>{

        fun map(
            id: Int,
            owner_id: Int,
            type: Int,
            title: String,
            description: String,
            count: Int,
            followers: Int,
            plays: Int,
            create_time: Int,
            update_time: Int,
            genres: List<Any>,
            is_following: Boolean,
            photo: Photo?=null,
            permissions: Any,
            subtitle_badge: Boolean,
            play_button: Boolean,
            access_key: String,
            album_type: String,
            exclusive: Boolean
        ):T

    }


    class ToPlaylistUiMapper @Inject constructor(
        private val managerResource: ManagerResource
    ): Mapper<PlaylistUi>{
        override fun map(
            id: Int,
            owner_id: Int,
            type: Int,
            title: String,
            description: String,
            count: Int,
            followers: Int,
            plays: Int,
            create_time: Int,
            update_time: Int,
            genres: List<Any>,
            is_following: Boolean,
            photo: Photo?,
            permissions: Any,
            subtitle_badge: Boolean,
            play_button: Boolean,
            access_key: String,
            album_type: String,
            exclusive: Boolean,
        ): PlaylistUi {
            val listOfStates = emptyList<PlaylistThumbsState>().toMutableList()

            if(photo==null) listOfStates.add(PlaylistThumbsState.LoadImages("" + R.drawable.im))
            else listOfStates.add(PlaylistThumbsState.LoadImages(photo.photo_300))

            repeat(amount_of_images_need-listOfStates.size){
                listOfStates.add(PlaylistThumbsState.Empty)
            }

            return PlaylistUi(
                playlistId = id.toString(),
                title = title,
                isFollowing = is_following,
                count = count,
                description = if(description.isEmpty()) managerResource.getString(R.string.no_description) else description,
                ownerId = owner_id,
                thumbStates = listOfStates
            )
        }
    }


    class ToPlaylistDomainMapper @Inject constructor(): Mapper<PlaylistDomain>{
        override fun map(
            id: Int,
            owner_id: Int,
            type: Int,
            title: String,
            description: String,
            count: Int,
            followers: Int,
            plays: Int,
            create_time: Int,
            update_time: Int,
            genres: List<Any>,
            is_following: Boolean,
            photo: Photo?,
            permissions: Any,
            subtitle_badge: Boolean,
            play_button: Boolean,
            access_key: String,
            album_type: String,
            exclusive: Boolean,
        ): PlaylistDomain {
            return PlaylistDomain(
                playlistId = id.toString(),
                title = title,
                isFollowing = is_following,
                count = count,
                description = description,
                ownerId = owner_id,
                thumbs = listOf(photo?.photo_300?:"")
            )
        }

    }


    class ToPlaylistCache(
        private val friendId: String
    ): Mapper<PlaylistCache>{
        override fun map(
            id: Int,
            owner_id: Int,
            type: Int,
            title: String,
            description: String,
            count: Int,
            followers: Int,
            plays: Int,
            create_time: Int,
            update_time: Int,
            genres: List<Any>,
            is_following: Boolean,
            photo: Photo?,
            permissions: Any,
            subtitle_badge: Boolean,
            play_button: Boolean,
            access_key: String,
            album_type: String,
            exclusive: Boolean,
        ): PlaylistCache {
            return PlaylistCache(
                playlistId = id.toString()+friendId,
                title = title,
                is_following = is_following,
                count = count,
                update_time = update_time,
                description = description,
                owner_id = owner_id,
                thumbs = listOf(photo?.photo_300?:"")
            )
        }

    }

}






