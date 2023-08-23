package com.kamancho.melisma.favoritesplaylistdetails.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.databinding.PlaylistDetailsTopbarBinding
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi

/**
 * Created by HP on 04.08.2023.
 **/
class PlaylistDetailsBarAdapter(
    private val context: Context,
    private val imageLoader: ImageLoader,
    private val diskCacheStrategy: DiskCacheStrategy
): RecyclerView.Adapter<PlaylistTopBarViewHolder>() {

    private var item = emptyList<PlaylistUi>().toMutableList()
    private lateinit var playlist: PlaylistUi

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTopBarViewHolder {
       return PlaylistTopBarViewHolder(
            context,
            PlaylistDetailsTopbarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),imageLoader, diskCacheStrategy,
        )
    }

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: PlaylistTopBarViewHolder, position: Int) {
        holder.bind(item[position])
    }

    fun setupPlaylist(playlistUi: PlaylistUi){
        playlist = playlistUi
        item.clear()
        item.add(playlistUi)
        notifyDataSetChanged()
    }

    fun showTopBar(show: Boolean){
        if(show && item.isEmpty()) {
            item.add(playlist)
            notifyItemInserted(0)
        }
        else if(!show && item.isNotEmpty()) {
            item.clear()
            notifyItemRemoved(0)
        }else if(show && item.isNotEmpty()) notifyItemChanged(0)
    }
}