package com.kamancho.melisma.trending.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.databinding.TrendingTopBarItemBinding

/**
 * Created by HP on 30.01.2023.
 **/

class TrendingTopBarAdapter(
    private val imageLoader: ImageLoader,
    private val navController: NavController

): RecyclerView.Adapter<TopBarViewHolder>(), Mapper<List<TrendingTopBarItemUi>, Unit> {

    private val playlists = mutableListOf<TrendingTopBarItemUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopBarViewHolder {

        return TopBarViewHolder(
            TrendingTopBarItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), navController, imageLoader
            )

    }

    override fun getItemCount(): Int = playlists.size



    override fun onBindViewHolder(holder: TopBarViewHolder, position: Int) {
       holder.bind(playlists[position])
    }

    override  fun map(data: List<TrendingTopBarItemUi>) {
        val diff = PlaylistDiffUtilCallback(data, playlists)
        val result = DiffUtil.calculateDiff(diff)
        playlists.clear()
        playlists.addAll(data)
        result.dispatchUpdatesTo(this)
    }


}


class TopBarViewHolder(
    private val binding: TrendingTopBarItemBinding,
    private val navController: NavController,
    private val imageLoader: ImageLoader
):ViewHolder(binding.root){

    private val mapper = TrendingTopBarItemUi.ListItemUi(binding, imageLoader, navController)

    fun bind(playlistUi: TrendingTopBarItemUi){
        playlistUi.map(mapper)
    }

}

class PlaylistDiffUtilCallback(
    private val newList: List<TrendingTopBarItemUi>,
    private val oldList: List<TrendingTopBarItemUi>
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