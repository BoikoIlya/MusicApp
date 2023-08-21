package com.example.musicapp.search.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Selector
import com.example.musicapp.databinding.PlaylistBinding
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import com.example.musicapp.userplaylists.presentation.PlaylistsViewHolder

/**
 * Created by HP on 01.05.2023.
 **/
class SearchPlaylistsPagingAdapter(
    private val selectListener: Selector<PlaylistUi>,
    private val clickClickListener: ClickListener<PlaylistUi>,
    private val imageLoader: ImageLoader,
): PagingDataAdapter<PlaylistUi, PlaylistsViewHolder>(
    object : DiffUtil.ItemCallback<PlaylistUi>(){
        override fun areItemsTheSame(oldItem: PlaylistUi, newItem: PlaylistUi): Boolean =
            oldItem.map(newItem)

        override fun areContentsTheSame(oldItem: PlaylistUi, newItem: PlaylistUi): Boolean =
                        oldItem == newItem

    }
) {

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        getItem(position)?.let{
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(
            PlaylistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), clickClickListener,selectListener,imageLoader, DiskCacheStrategy.NONE
        )
    }

}