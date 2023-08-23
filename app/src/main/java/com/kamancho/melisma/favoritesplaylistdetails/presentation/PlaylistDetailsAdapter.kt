package com.kamancho.melisma.favoritesplaylistdetails.presentation

import android.content.Context
import android.view.View
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.databinding.PlaylistDetailsTopbarBinding
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi

/**
 * Created by HP on 02.08.2023.
 **/

abstract class AdapterViewHolder(root: View): ViewHolder(root){

   open fun bind(item: MediaItem, position: Int, selectedPosition: Int) = Unit

   open fun bind(item: PlaylistUi) = Unit

}

class PlaylistTopBarViewHolder(
    context: Context,
    private val  binding: PlaylistDetailsTopbarBinding,
    imageLoader: ImageLoader,
    cacheStrategy: DiskCacheStrategy,
): AdapterViewHolder(binding.root){

    private val mapper = PlaylistUi.ShowToUiPlaylistDetails(binding,imageLoader,context,cacheStrategy)

    override fun bind(item: PlaylistUi) {
        item.map(mapper)
    }

}