package com.example.musicapp.userplaylists.presentation

import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.databinding.PlaylistBinding
import com.example.musicapp.databinding.PlaylistDataFragmentBinding
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/


data class PlaylistUi(
    private val playlistId: Int,
    private val title: String,
    private val is_following: Boolean,
    private val count: Int,
    private val description: String,
    private val owner_id: Int,
    private val thumbStates: List<PlaylistThumbsState>
){

    fun map(item: PlaylistUi):Boolean =  playlistId == item.playlistId

    fun<T>map(mapper: Mapper<T>): T = mapper.map(playlistId, title, is_following, count, description, owner_id, thumbStates)

    interface Mapper<T>{

        fun map(
            playlistId: Int,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>
        ): T
    }


    class ShowToUiMapper(
        private val binding: PlaylistBinding,
        private val imageLoader: ImageLoader,
    ): Mapper<Unit>{

        private val imageViewList =  listOf(
            binding.leftImgUpper,
            binding.rightImgUpper,
            binding.rightImgLower,
            binding.leftImgLower)

        override fun map(
            playlistId: Int,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ) = with(binding){
            playlistName.text = title
            var i = 0
            while (i<imageViewList.size){
                thumbStates[i].apply(imageViewList[i],imageLoader)
                i++
            }

        }

    }

    class ToDomainMapper @Inject constructor(): Mapper<PlaylistDomain> {

        override fun map(
            playlistId: Int,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ): PlaylistDomain = PlaylistDomain(
            playlistId = playlistId,
            title = title,
            is_following = is_following,
            count = count,
            description = description,
            owner_id = owner_id,
            thumbs = thumbStates.map { it.map() }.filter { it!="" }

        )
    }

    class ToIdMapper @Inject constructor(): Mapper<Int> {
        override fun map(
            playlistId: Int,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ): Int = playlistId
    }

    class IsPlaylistDataChanged(
        private val newTitle: String,
        private val newDescription: String,
    ): Mapper<Boolean> {
        override fun map(
            playlistId: Int,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ): Boolean = newTitle.trim()!=title ||newDescription.trim() !=description

    }

    class ToEditPlaylistData(
        private val binding: PlaylistDataFragmentBinding
    ): Mapper<Unit> {
        override fun map(
            playlistId: Int,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ){
            binding.titleEditText.setText(title)
            binding.descriptionEditText.setText(description)
        }

    }

    class CanEdit @Inject constructor(): Mapper<Boolean> {
        override fun map(
            playlistId: Int,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ): Boolean = !is_following
    }
}
