package com.example.musicapp.trending.presentation

import androidx.navigation.NavController
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.databinding.TrendingTopBarItemBinding
import com.example.musicapp.trending.data.TrendingRepository

data class TrendingTopBarItemUi(
    private val id: String,
    private val title: String,
    private val imgUrl: String,
    private val navigationState: TrendingTopBarNavigationState
){

    interface Mapper<T>{
        fun map(
            id: String,
            title: String,
            imgUrl: String,
            navigationState: TrendingTopBarNavigationState
        ): T
    }

    fun <T>map(mapper: Mapper<T>): T = mapper.map(id, title, imgUrl,navigationState)

    class ListItemUi(
        private val binding: TrendingTopBarItemBinding,
        private val imageLoader: ImageLoader,
        private val navController: NavController
    ): Mapper<Unit>{
        override fun map(
            id: String,
            title: String,
            imgUrl: String,
            navigationState: TrendingTopBarNavigationState
        ) {
            with(binding){

                imageLoader.loadImage(imgUrl,playlistImg,cacheStrategy = DiskCacheStrategy.AUTOMATIC)
                playlistName.text = title
                root.setOnClickListener {
                    navigationState.apply(navController)
                }
            }
        }

    }


    fun map(item: TrendingTopBarItemUi):Boolean =  id == item.id

}
