package com.kamancho.melisma.userplaylists.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.app.core.Selector
import com.kamancho.melisma.databinding.PlaylistBinding

/**
 * Created by HP on 14.07.2023.
 **/
class PlaylistsAdapter(
    private val imageLoader: ImageLoader,
    private val clickListener: ClickListener<PlaylistUi>,
    private val selector: Selector<PlaylistUi>
    ): RecyclerView.Adapter<PlaylistsViewHolder>(), Mapper<List<PlaylistUi>, Unit> {

    private val playlists = mutableListOf<PlaylistUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(
            PlaylistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), clickListener,selector, imageLoader, DiskCacheStrategy.AUTOMATIC
        )
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override  fun map(data: List<PlaylistUi>) {
        val diff = PlaylistsDiffUtilCallback(data, playlists)
        val result = DiffUtil.calculateDiff(diff)
        playlists.clear()
        playlists.addAll(data)
        result.dispatchUpdatesTo(this)
    }


}

class PlaylistsViewHolder(
    private val binding: PlaylistBinding,
    private val clickListener: ClickListener<PlaylistUi>,
    private val selector: Selector<PlaylistUi>,
    imageLoader: ImageLoader,
    cacheStrategy: DiskCacheStrategy
): RecyclerView.ViewHolder(binding.root){

    private val mapper = PlaylistUi.ShowToUiMapper(binding,imageLoader,cacheStrategy)

    fun bind(item: PlaylistUi){
        item.map(mapper)
        binding.root.setOnClickListener{
            clickListener.onClick(item)
        }
        binding.root.setOnLongClickListener {
            selector.onSelect(item,0)
            return@setOnLongClickListener true
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