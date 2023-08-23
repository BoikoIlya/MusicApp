package com.example.musicapp.trending.presentation

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.app.core.DeleteItemDialog
import com.example.musicapp.app.core.Selector
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.is_cached
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.small_img_url
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.track_duration_formatted
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.track_id

import com.example.musicapp.databinding.TrackItemBinding
import com.example.musicapp.favoritesplaylistdetails.presentation.AdapterViewHolder

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
    fun removeFromAdapter(viewModel: DeleteItemDialog, position: Int)
}

interface Navigator{

    fun navigateToMenu(data: MediaItem,position: Int)

    object Empty: Navigator {
        override fun navigateToMenu(data: MediaItem,position: Int) = Unit
    }
}

interface MediaItemsAdapter: Navigator,RemoveItem


open class TracksAdapter(
    private val context: Context,
    private val playClickListener:Selector<MediaItem> ,
    private val saveClickListener: ClickListener<MediaItem>,
    private val imageLoader: ImageLoader,
    private val addBtnVisibility: Int = View.VISIBLE,
    private val navigator: Navigator = Navigator.Empty,
    private val cacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE,
    private val layoutManager: LayoutManager
 ): RecyclerView.Adapter<AdapterViewHolder>(),
    Mapper<List<MediaItem>,Unit>,Select, Scroller, MediaItemsAdapter {

    protected var selectedTrackPosition = -1
    protected var selectedTrack: MediaItem? = null //For case when selected track will collect faster than tracks
    private val diff = AsyncListDiffer(this,TracksDiffUtilItemCallback())
    private var recyclerViewState: Parcelable? = null

    init {
        diff.addListListener { previousList, currentList ->
            layoutManager.onRestoreInstanceState(recyclerViewState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        return TracksViewHolder(
            context,
            TrackItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), playClickListener, saveClickListener,imageLoader,addBtnVisibility, cacheStrategy
        )
    }

    override fun getItemCount(): Int = diff.currentList.size

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
            holder.bind(diff.currentList[position], position, selectedTrackPosition)
    }


    override fun map(data: List<MediaItem>) {
        recyclerViewState = layoutManager.onSaveInstanceState()
        diff.submitList(data)

        if(selectedTrack!=null) newPosition(selectedTrack!!)
        else selectedTrack = null
    }


    override fun newPosition(mediaItem: MediaItem) {
        if (mediaItem.mediaId.isEmpty()) return
        val old = selectedTrackPosition

        val position = diff.currentList.indexOfFirst { it.mediaId == mediaItem.mediaId }
        if (position!=-1 ){
            selectedTrackPosition = position
            notifyItemChanged(position)
        }else {
            selectedTrackPosition = -1
        }
        selectedTrack = mediaItem
        notifyItemChanged(old)
    }

    override fun scrollToSelectedTrack(rcv: RecyclerView) {
            rcv.scrollToPosition(selectedTrackPosition)

    }


    override fun navigateToMenu(data: MediaItem, position: Int) {
        navigator.navigateToMenu(diff.currentList[position],position)
    }

    override fun removeFromAdapter(viewModel: DeleteItemDialog, position: Int) {
        viewModel.launchDeleteItemDialog(diff.currentList[position])
    }


}



open class TracksViewHolder(
    private val context: Context,
    private val binding: TrackItemBinding,
    private val playClickListener: Selector<MediaItem>,
    private val saveClickListener: ClickListener<MediaItem>,
    private val imageLoader: ImageLoader,
    private val addBtnVisibility: Int,
    private val cacheStrategy: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC
): AdapterViewHolder(binding.root) {


   override fun bind(item: MediaItem, position: Int, selectedPosition: Int) = with(binding){
        with(item.mediaMetadata) {
            imageLoader.loadImage(
                extras?.getString(small_img_url)?:"",
                trackImg, cacheStrategy = cacheStrategy)
            songNameTv.text = title?.take(50)
            authorTv.text = context.getString(R.string.divider_dot) + artist?.take(50)
            trackDurationTv.text =extras?.getString(track_duration_formatted)?:""
            addBtn.visibility = addBtnVisibility
            downloadedIcon.isVisible =extras?.getBoolean(is_cached)?:false
            songNameTv.setTextColor(context.getColor(if(!isPlayable!!) R.color.gray else R.color.black))
        }

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


class TracksDiffUtilItemCallback: DiffUtil.ItemCallback<MediaItem>(){

    override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
       return newItem.mediaId == oldItem.mediaId
    }

    override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
        return  newItem.mediaId==oldItem.mediaId
                && newItem.mediaMetadata.title==oldItem.mediaMetadata.title
                && newItem.mediaMetadata.artist==oldItem.mediaMetadata.artist
                && newItem.mediaMetadata.artworkUri==oldItem.mediaMetadata.artworkUri
                && newItem.mediaMetadata.albumTitle==oldItem.mediaMetadata.albumTitle
                && newItem.mediaMetadata.isPlayable==oldItem.mediaMetadata.isPlayable
                && newItem.mediaMetadata.extras?.getString(track_id)==oldItem.mediaMetadata.extras?.getString(track_id)
                && newItem.mediaMetadata.extras?.getBoolean(is_cached)==oldItem.mediaMetadata.extras?.getBoolean(is_cached)
    }

}