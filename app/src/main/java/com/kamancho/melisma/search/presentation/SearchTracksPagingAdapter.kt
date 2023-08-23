package com.kamancho.melisma.search.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.Selector
import com.kamancho.melisma.app.core.ToMediaItemMapper.Base.Companion.track_id
import com.kamancho.melisma.databinding.TrackItemBinding
import com.kamancho.melisma.trending.presentation.TracksViewHolder

/**
 * Created by HP on 01.05.2023.
 **/
class SearchTracksPagingAdapter(
    private val context: Context,
    private val playClickListener: Selector<MediaItem>,
    private val saveClickListener: ClickListener<MediaItem>,
    private val imageLoader: ImageLoader,
    private val addBtnVisibility: Int = View.VISIBLE
): PagingDataAdapter<MediaItem, TracksViewHolder>(
    object : DiffUtil.ItemCallback<MediaItem>(){
        override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
            newItem.mediaMetadata.extras?.getInt(track_id) ==oldItem.mediaMetadata.extras?.getInt(track_id)



        override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
                        newItem.mediaId==oldItem.mediaId
                    && newItem.mediaMetadata.title==oldItem.mediaMetadata.title
                    && newItem.mediaMetadata.artist==oldItem.mediaMetadata.artist
                    && newItem.mediaMetadata.artworkUri==oldItem.mediaMetadata.artworkUri
                    && newItem.mediaMetadata.albumTitle==oldItem.mediaMetadata.albumTitle
                    && newItem.mediaMetadata.isPlayable==oldItem.mediaMetadata.isPlayable
                    && newItem.mediaMetadata.extras?.getInt(track_id)==oldItem.mediaMetadata.extras?.getInt(track_id)

    }
) {
    private var selectedTrackPosition = -1
    private var selectedTrack: MediaItem? = null //For case when selected track will collect faster than tracks

    init {
        addLoadStateListener {
            if(selectedTrack!=null) newPosition(selectedTrack!!)
            else selectedTrack = null
        }
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        getItem(position)?.let{
            holder.bind(it, position, selectedTrackPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(
            context,
            TrackItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), playClickListener, saveClickListener,imageLoader,addBtnVisibility
        )
    }

     fun newPosition(mediaItem: MediaItem) {
        if (mediaItem.mediaId.isEmpty()) return
        val old = selectedTrackPosition
        val position = this.snapshot().items.indexOfFirst{ it.mediaId == mediaItem.mediaId}
        if (position!=-1 ){
            selectedTrackPosition = position
            notifyItemChanged(position)
        }else {
            selectedTrackPosition = -1
        }
         selectedTrack = mediaItem
        notifyItemChanged(old)
    }
}