package com.example.musicapp.trending.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.databinding.PlaylistItemBinding

/**
 * Created by HP on 30.01.2023.
 **/

class PlaylistsAdapter(
    private val imageLoader: ImageLoader,
    private val clickListener: ClickListener<PlaylistUi>
): RecyclerView.Adapter<PlaylistsViewHolder>(), Mapper<List<PlaylistUi>, Unit> {

    private val playlists = mutableListOf<PlaylistUi>()
    private var selectedItemPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(
            PlaylistItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), clickListener, imageLoader
        )
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
       holder.bind(playlists[position])
    }

    override fun map(data: List<PlaylistUi>) {
        val diff = PlaylistsDiffUtilCallback(data, playlists)
        val result = DiffUtil.calculateDiff(diff)
        playlists.clear()
        playlists.addAll(data)
        result.dispatchUpdatesTo(this)
    }


}

class PlaylistsViewHolder(
    private val binding: PlaylistItemBinding,
    private val clickListener: ClickListener<PlaylistUi>,
    private val imageLoader: ImageLoader
): RecyclerView.ViewHolder(binding.root){

    private val mapper = PlaylistUi.ListItemUi(binding, imageLoader)

    fun bind(item: PlaylistUi){
        item.map(mapper)
        binding.root.setOnClickListener {
            clickListener.onClick(item)
        }
    }
}

class PlaylistsDiffUtilCallback(
    private val newList: List<PlaylistUi>,
    private val oldList: List<PlaylistUi>
): DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].map(oldList[oldItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition] == oldList[oldItemPosition]
    }

}