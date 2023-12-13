package com.kamancho.melisma.trending.presentation

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.BaseViewModel
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.app.core.DeleteItemDialog
import com.kamancho.melisma.app.core.Selector
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.is_cached
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.small_img_url
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.track_duration_formatted
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.track_id

import com.kamancho.melisma.databinding.TrackItemBinding
import com.kamancho.melisma.favoritesplaylistdetails.presentation.AdapterViewHolder

/**
 * Created by HP on 30.01.2023.
 **/
interface Select{
    fun newPosition(mediaItem: MediaItem)
}

interface Scroller{
    fun scrollToSelectedTrack(rcv: RecyclerView)
}

interface RightSwipe{
    fun onRightSwipe(viewModel: DeleteItemDialog, position: Int)
}

interface LeftSwipe{

    fun onLeftSwipe(data: MediaItem, position: Int)

    object Empty: LeftSwipe {
        override fun onLeftSwipe(data: MediaItem, position: Int) = Unit
    }
}

interface MediaItemsAdapter: LeftSwipe,RightSwipe


open class TracksAdapter(
    private val context: Context,
    private val playClickListener:Selector<MediaItem>,
    private val saveClickListener: ClickListener<MediaItem>,
    private val imageLoader: ImageLoader,
    private val addBtnVisibility: Int = View.VISIBLE,
    private val leftSwipe: LeftSwipe = LeftSwipe.Empty,
    private val cacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE,
    private val layoutManager: LayoutManager,
    private val onLongClick:((MediaItem)->Unit)? = null,
    private val tracksStartPositionInRecycler: Int = 0
 ): RecyclerView.Adapter<AdapterViewHolder>(),
    Mapper<List<MediaItem>,Unit>,Select, Scroller, MediaItemsAdapter {

    protected var selectedTrackPosition = -1
    protected var selectedTrack: MediaItem? = null //For case when selected track will collect faster than tracks
    private val diff = AsyncListDiffer(this,TracksDiffUtilItemCallback())
    private var recyclerViewState: Parcelable? = null

    init {
        diff.addListListener { _, _ ->
            layoutManager.onRestoreInstanceState(recyclerViewState)
            if(selectedTrack!=null) newPosition(selectedTrack!!)
            else selectedTrack = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        return TracksViewHolder(
            context,
            TrackItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), playClickListener, saveClickListener,imageLoader,
            addBtnVisibility, cacheStrategy,onLongClick
        )
    }

    override fun getItemCount(): Int = diff.currentList.size

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
            holder.bind(diff.currentList[position], position, selectedTrackPosition)
    }


    override fun map(data: List<MediaItem>) {
        recyclerViewState = layoutManager.onSaveInstanceState()
        diff.submitList(data)
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


    override fun onLeftSwipe(data: MediaItem, position: Int) {
        leftSwipe.onLeftSwipe(
            diff.currentList[position-tracksStartPositionInRecycler],
            position-tracksStartPositionInRecycler
        )
    }

    override fun onRightSwipe(viewModel: DeleteItemDialog, position: Int) {
        viewModel.launchDeleteItemDialog(diff.currentList[position-tracksStartPositionInRecycler])
    }

    fun <T>saveCurrPageTracks(viewModel: BaseViewModel<T>){
        viewModel.saveCurrentPageQueue(diff.currentList)
    }

}



open class TracksViewHolder(
    private val context: Context,
    private val binding: TrackItemBinding,
    private val playClickListener: Selector<MediaItem>,
    private val saveClickListener: ClickListener<MediaItem>,
    private val imageLoader: ImageLoader,
    private val addBtnVisibility: Int,
    private val cacheStrategy: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC,
    private val onLongClick:((MediaItem)->Unit)? = null
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

       binding.root.setOnLongClickListener {
           onLongClick?.invoke(item)
           return@setOnLongClickListener true
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