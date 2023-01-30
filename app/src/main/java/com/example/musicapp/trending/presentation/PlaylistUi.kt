package com.example.musicapp.trending.presentation

import com.bumptech.glide.Glide
import com.example.musicapp.ImageLoader
import com.example.musicapp.R
import com.example.musicapp.databinding.PlaylistItemBinding

data class PlaylistUi(
    private val id: String,
    private val name: String,
    private val descriptions: String,
    private val imgUrl: String,
    private val tracksUrl: String
){

    interface Mapper<T>{
        fun map(
            id: String,
            name: String,
            descriptions: String,
            imgUrl: String,
            tracksUrl: String
        ): T
    }

    fun <T>map(mapper: Mapper<T>): T = mapper.map(id, name, descriptions, imgUrl, tracksUrl)

    class ListItemUi(
        private val binding: PlaylistItemBinding,
        private val imageLoader: ImageLoader
    ): Mapper<Unit>{
        override fun map(
            id: String,
            name: String,
            descriptions: String,
            imgUrl: String,
            tracksUrl: String,
        ) {
            with(binding){
                imageLoader.loadImage(imgUrl,playlistImg, R.drawable.playlist)
                playlistName.text = name
            }
        }

    }

    fun map(item: PlaylistUi):Boolean =  id == item.id

}
