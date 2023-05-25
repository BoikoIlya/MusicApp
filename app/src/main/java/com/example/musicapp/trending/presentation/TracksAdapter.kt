package com.example.musicapp.trending.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.musicapp.R
import com.example.musicapp.app.core.*
import com.example.musicapp.databinding.TrackItemBinding

/**
 * Created by HP on 30.01.2023.
 **/
interface Select{
    fun newPosition(mediaItem: MediaItem)
}

interface Scroller{
    fun scrollToSelectedTrack(rcv: RecyclerView)
}

interface RemoveItem{
    fun removeFromAdapter(viewModel: Remover, position: Int)
}
 class TracksAdapter(
    private val context: Context,
    private val playClickListener: Selector<MediaItem>,
    private val saveClickListener: ClickListener<MediaItem>,
    private val imageLoader: ImageLoader,
    private val addBtnVisibility: Int = View.VISIBLE
): RecyclerView.Adapter<TrendingTracksViewHolder>(),
    Mapper<List<MediaItem>, Unit>,Select, Scroller, RemoveItem {

    private val tracksCurrentList = mutableListOf<MediaItem>()
    protected var selectedTrackPosition = -1
    protected var selectedTrack: MediaItem? = null //For case when selected track will collect faster than tracks


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingTracksViewHolder {
        return TrendingTracksViewHolder(
            context,
            TrackItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), playClickListener, saveClickListener,imageLoader,addBtnVisibility
        )
    }

    override fun getItemCount(): Int = tracksCurrentList.size

    override fun onBindViewHolder(holder: TrendingTracksViewHolder, position: Int) {
            holder.bind(tracksCurrentList[position], position, selectedTrackPosition)
    }

    private lateinit var result: DiffUtil.DiffResult
    override  fun map(data: List<MediaItem>) {
        val diff = TendingTracksDiffUtilCallback(data, tracksCurrentList)
        result = DiffUtil.calculateDiff(diff)
        tracksCurrentList.clear()
        tracksCurrentList.addAll(data)
        if (selectedTrackPosition != -1)
            selectedTrackPosition = result.convertOldPositionToNew(selectedTrackPosition)

        result.dispatchUpdatesTo(this@TracksAdapter)

        if(selectedTrack!=null) newPosition(selectedTrack!!)
        else selectedTrack = null
    }


    override fun newPosition(mediaItem: MediaItem) {
        if (mediaItem.mediaId.isEmpty()) return
        val old = selectedTrackPosition
        val position = tracksCurrentList.indexOf(mediaItem)
        if (position!=-1 ){
            selectedTrackPosition = position
            notifyItemChanged(position)
        }else {
            selectedTrackPosition = -1
            selectedTrack = mediaItem
        }
        notifyItemChanged(old)
    }

    override fun scrollToSelectedTrack(rcv: RecyclerView) {
        rcv.smoothScrollToPosition(selectedTrackPosition)

    }

    override fun removeFromAdapter(viewModel: Remover, position: Int) {
        viewModel.removeItem(tracksCurrentList[position].mediaId)
    }



}



class TrendingTracksViewHolder(
    private val context: Context,
    private val binding: TrackItemBinding,
    private val playClickListener: Selector<MediaItem>,
    private val saveClickListener: ClickListener<MediaItem>,
    private val imageLoader: ImageLoader,
    private val addBtnVisibility: Int,
): ViewHolder(binding.root){

    fun bind(item: MediaItem, position: Int, selectedPosition: Int) =  with(binding){
        with(item.mediaMetadata) {
            imageLoader.loadImage("https://${artworkUri?.host}${artworkUri?.path}", trackImg)
            songNameTv.text = title
            authorNameTv.text = artist
            addBtn.visibility = addBtnVisibility
        }
        root.setBackgroundColor(context.getColor(
            if(selectedPosition==position) R.color.light_gray else R.color.white))

        binding.root.setOnClickListener {
            root.setBackgroundColor(context.getColor(R.color.light_gray))
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
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].mediaId == oldList[oldItemPosition].mediaId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return  newList[newItemPosition].mediaId==oldList[oldItemPosition].mediaId
                && newList[newItemPosition].mediaMetadata.title==oldList[oldItemPosition].mediaMetadata.title
                && newList[newItemPosition].mediaMetadata.artist==oldList[oldItemPosition].mediaMetadata.artist
                && newList[newItemPosition].mediaMetadata.artworkUri==oldList[oldItemPosition].mediaMetadata.artworkUri
                && newList[newItemPosition].mediaMetadata.albumTitle==oldList[oldItemPosition].mediaMetadata.albumTitle
    }

}