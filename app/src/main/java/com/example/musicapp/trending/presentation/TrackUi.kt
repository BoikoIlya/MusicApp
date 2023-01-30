package com.example.musicapp.trending.presentation

import com.example.musicapp.ImageLoader
import com.example.musicapp.R
import com.example.musicapp.databinding.PlaylistItemBinding
import com.example.musicapp.databinding.SongItemBinding
import java.lang.ref.SoftReference

data class TrackUi(
    private val id: String,
    private val playbackMinutes: String,
    private val name: String,
    private val artistName: String,
    private val previewURL: String,
    private val albumName: String
){

    interface Mapper<T>{
        fun map(
            id: String,
            playbackMinutes: String,
            name: String,
            artistName: String,
            previewURL: String,
            albumName: String
        ): T
    }

    fun <T>map(mapper: Mapper<T>): T = mapper.map(id, playbackMinutes, name, artistName, previewURL, albumName)

    class ListItemUi(
        private val binding: SongItemBinding,
    ): Mapper<Unit>{
        override fun map(
            id: String,
            playbackMinutes: String,
            name: String,
            artistName: String,
            previewURL: String,
            albumName: String,
        ) {
            with(binding){
               playbackTimeTv.text = playbackMinutes
                songNameTv.text = name
                authorNameTv.text = artistName
            }
        }


    }

    fun map(item: TrackUi):Boolean =  id == item.id


}
