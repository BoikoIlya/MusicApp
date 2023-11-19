package com.kamancho.melisma.userplaylists.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kamancho.melisma.databinding.PlaylistSectionBinding
import com.kamancho.melisma.trending.presentation.TrendingTopBarAdapter

/**
 * Created by HP on 07.08.2023.
 **/
class PlaylistSectionAdapter(
    private val playlistAdapter: TrendingTopBarAdapter,
    private val context: Context
): RecyclerView.Adapter<PlaylistSectionViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSectionViewHolder {
        return PlaylistSectionViewHolder(
            binding = PlaylistSectionBinding.inflate(
                LayoutInflater.from(parent.context),parent,false),
            context = context,)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: PlaylistSectionViewHolder, position: Int) {
        holder.bind(playlistAdapter)
    }


}


class PlaylistSectionViewHolder(
    private val binding: PlaylistSectionBinding,
    private val context: Context,
): ViewHolder(binding.root){

    fun bind(adapter: TrendingTopBarAdapter){
        binding.root.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        (binding.root.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        binding.root.adapter = adapter
    }

}