package com.example.musicapp.trending.presentation

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.musicapp.R
import com.example.musicapp.app.core.*
import com.example.musicapp.databinding.TrackItemBinding
import kotlin.math.log

/**
 * Created by HP on 30.01.2023.
 **/
interface Select{
    fun newPosition(mediaItem: MediaItem)
}
class TrendingTracksAdapter(
    private val context: Context,
    private val playClickListener: Selector<MediaItem>,
    private val saveClickListener: ClickListener<MediaItem>,
    private val mapper: TrackUi.Mapper<TrackUi>,
    private val imageLoader: ImageLoader
): RecyclerView.Adapter<TrendingTracksViewHolder>(),
    Mapper<List<MediaItem>, Unit>,Select {

    private val tracks = mutableListOf<MediaItem>()
    private var selectedTrackPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingTracksViewHolder {
        return TrendingTracksViewHolder(
            context,
            TrackItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), playClickListener, saveClickListener,imageLoader
        )
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrendingTracksViewHolder, position: Int) {
        holder.bind(tracks[position], position, selectedTrackPosition)
    }

    override fun map(data: List<MediaItem>) {
        val diff = TendingTracksDiffUtilCallback(data, tracks)
        val result = DiffUtil.calculateDiff(diff)
        tracks.clear()
        tracks.addAll(data)
        result.dispatchUpdatesTo(this)
    }

    override fun newPosition(mediaItem: MediaItem) {
        if (mediaItem.mediaId.isEmpty()) return
        val old = selectedTrackPosition
        val position = tracks.indexOf(mediaItem)
        if (position!=-1 ){
            selectedTrackPosition = position

            if(old!=-1) notifyItemChanged(old)
            notifyItemChanged(position)
        }
    }

}

class TrendingTracksViewHolder(
    private val context: Context,
    private val binding: TrackItemBinding,
    private val playClickListener: Selector<MediaItem>,
    private val saveClickListener: ClickListener<MediaItem>,
    private val imageLoader: ImageLoader,
): ViewHolder(binding.root){


    fun bind(item: MediaItem, position: Int, selectedPosition: Int) =  with(binding){
        imageLoader.loadImage(
            "https://"+
            item.mediaMetadata.artworkUri?.host+
            item.mediaMetadata.artworkUri?.path,
            trackImg)
        songNameTv.text = item.mediaMetadata.title
        authorNameTv.text = item.mediaMetadata.artist
        root.setBackgroundColor(context.getColor(
            if(selectedPosition==position) R.color.light_gray else R.color.white))

        binding.root.setOnClickListener {
            playClickListener.onSelect(item, position)
        }

        binding.addBtn.setOnClickListener {
            saveClickListener.onClick(item)
        }
    }

}

class TendingTracksDiffUtilCallback(
    private val newList: List<MediaItem>,
    private val oldList: List<MediaItem>
): DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].mediaId == oldList[oldItemPosition].mediaId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition] == oldList[oldItemPosition]
    }

}