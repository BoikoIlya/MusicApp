package com.example.musicapp.trending.presentation

import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.databinding.PlaylistItemBinding

data class PlaylistUi(
    private val id: String,
    private val name: String,
    private val imgUrl: String,
    private val tracksUrl: String
){

    interface Mapper<T>{
        fun map(
            id: String,
            name: String,
            imgUrl: String,
            tracksUrl: String
        ): T
    }

    fun <T>map(mapper: Mapper<T>): T = mapper.map(id, name, imgUrl, tracksUrl)

    class ListItemUi(
        private val binding: PlaylistItemBinding,
        private val imageLoader: ImageLoader,
        private val clickListener: ClickListener<String>
    ): Mapper<Unit>{
        override fun map(
            id: String,
            name: String,
            imgUrl: String,
            tracksUrl: String,
        ) {
            with(binding){

                imageLoader.loadImage(imgUrl,playlistImg)
                playlistName.text = name
                root.setOnClickListener { clickListener.onClick(id) }
            }
        }

    }


    fun map(item: PlaylistUi):Boolean =  id == item.id

}
