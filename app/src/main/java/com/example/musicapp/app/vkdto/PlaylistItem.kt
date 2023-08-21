package com.example.musicapp.app.vkdto

import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import javax.inject.Inject

data class PlaylistItem(
   private val access_key: String,
   private val album_type: String,
   private val count: Int,
   private val create_time: Int,
   private val description: String,
   private val exclusive: Boolean,
   private val followers: Int,
   private val genres: List<Any>,
   private val photo: Photo?=null,
   private val id: Int,
   private val is_following: Boolean,
   private val owner_id: Int,
   private val permissions: Permissions,
   private val play_button: Boolean,
   private val plays: Int,
   private val subtitle_badge: Boolean,
   private val thumbs: List<PlaylistThumb>,
   private val title: String,
   private val type: Int,
   private val update_time: Int
){

    fun <T>map(mapper: Mapper<T>):T = mapper.map(
        access_key,
        album_type,
        count,
        create_time,
        description,
        exclusive,
        followers,
        genres,
        photo,
        id,
        is_following,
        owner_id,
        permissions,
        play_button,
        plays,
        subtitle_badge,
        thumbs,
        title,
        type,
        update_time
    )

    interface Mapper<T>{

        fun map(
            access_key: String,
            album_type: String,
            count: Int,
            create_time: Int,
            description: String,
            exclusive: Boolean,
            followers: Int,
            genres: List<Any>,
            photo: Photo?,
            id: Int,
            is_following: Boolean,
            owner_id: Int,
            permissions: Permissions,
            play_button: Boolean,
            plays: Int,
            subtitle_badge: Boolean,
            thumbs: List<PlaylistThumb>?=null,
            title: String,
            type: Int,
            update_time: Int
        ):T

    }

    class ToPlaylistCache @Inject constructor(): Mapper<PlaylistCache> {

        override fun map(
            access_key: String,
            album_type: String,
            count: Int,
            create_time: Int,
            description: String,
            exclusive: Boolean,
            followers: Int,
            genres: List<Any>,
            photo: Photo?,
            id: Int,
            is_following: Boolean,
            owner_id: Int,
            permissions: Permissions,
            play_button: Boolean,
            plays: Int,
            subtitle_badge: Boolean,
            thumbs: List<PlaylistThumb>?,
            title: String,
            type: Int,
            update_time: Int,
        ): PlaylistCache {

            val thumbsList = emptyList<String>().toMutableList()
            if(thumbs!=null){
                thumbs.forEach {
                    thumbsList.add(it.photo_300)
                }
            }else if(photo!=null) thumbsList.add(photo.photo_300)

           return PlaylistCache(
                playlistId = id.toString(),
                title =title,
                is_following = is_following,
                count = count,
                update_time = update_time,
                description = description,
                owner_id = owner_id,
                thumbs = thumbsList
            )
        }
    }



}