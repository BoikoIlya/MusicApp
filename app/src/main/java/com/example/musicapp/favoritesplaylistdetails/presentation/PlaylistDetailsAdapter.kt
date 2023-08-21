package com.example.musicapp.favoritesplaylistdetails.presentation

import android.content.Context
import android.view.View
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Selector
import com.example.musicapp.databinding.PlaylistDetailsTopbarBinding
import com.example.musicapp.trending.presentation.Navigator
import com.example.musicapp.trending.presentation.TracksAdapter
import com.example.musicapp.userplaylists.presentation.PlaylistUi

/**
 * Created by HP on 02.08.2023.
 **/
class PlaylistDetailsAdapter(
    private val context: Context,
    private val playClickListener: Selector<MediaItem>,
    private val saveClickListener: ClickListener<MediaItem>,
    private val imageLoader: ImageLoader,
    private val addBtnVisibility: Int = View.VISIBLE,
    private val navigator: Navigator = Navigator.Empty,
    private val cacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE,
    private val playlistUi: PlaylistUi
)//: TracksAdapter(context, playClickListener, saveClickListener, imageLoader, addBtnVisibility, navigator, cacheStrategy) //RecyclerView.Adapter<AdapterViewHolder>(),Mapper<List<PlaylistDetailsUi>,Unit>{
{
//    private var selectedTrackPosition = -1
//    private var selectedTrack: MediaItem? = null //For case when selected track will collect faster than tracks
//    private val list = emptyList<PlaylistDetailsUi>().toMutableList()


//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
//        return if(viewType==R.layout.playlist_details_topbar)
//             else super.onCreateViewHolder(parent, viewType)
//        else TracksViewHolder(
//            context,
//            TrackItemBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            ), playClickListener, saveClickListener,imageLoader,addBtnVisibility, cacheStrategy
        //)
    }


//    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
//        list[position].bind(holder,position,selectedTrackPosition)
//    }
//
//    override fun getItemViewType(position: Int): Int = list[position].viewType()

    //override fun getItemCount(): Int = list.size

//    override fun map(data: List<PlaylistDetailsUi>) {
////        val diff = TracksDiffUtilCallback(data, tracksCurrentList)
////        result = DiffUtil.calculateDiff(diff)
////        tracksCurrentList.clear()
////        tracksCurrentList.addAll(data)
////        result.dispatchUpdatesTo(this@TracksAdapter)
//
//        notifyDataSetChanged()
//
//        if(selectedTrack!=null) newPosition(selectedTrack!!)
//        else selectedTrack = null
//    }


//}

//class TracksDiffUtilCallback(
//    private val newList: List<PlaylistDetailsUi>,
//    private val oldList: List<PlaylistDetailsUi>,
//): DiffUtil.Callback() {
//
//    override fun getOldListSize(): Int = oldList.size
//
//    override fun getNewListSize(): Int = newList.size
//
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return newList[newItemPosition].mediaId == oldList[oldItemPosition].mediaId
//    }
//
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return  newList[newItemPosition].mediaId==oldList[oldItemPosition].mediaId
//                && newList[newItemPosition].mediaMetadata.title==oldList[oldItemPosition].mediaMetadata.title
//                && newList[newItemPosition].mediaMetadata.artist==oldList[oldItemPosition].mediaMetadata.artist
//                && newList[newItemPosition].mediaMetadata.artworkUri==oldList[oldItemPosition].mediaMetadata.artworkUri
//                && newList[newItemPosition].mediaMetadata.albumTitle==oldList[oldItemPosition].mediaMetadata.albumTitle
//                && newList[newItemPosition].mediaMetadata.isPlayable==oldList[oldItemPosition].mediaMetadata.isPlayable
//                && newList[newItemPosition].mediaMetadata.extras?.getInt(ToMediaItemMapper.track_id)==oldList[oldItemPosition].mediaMetadata.extras?.getInt(
//            ToMediaItemMapper.track_id
//        )
//    }
//}


abstract class AdapterViewHolder(root: View): ViewHolder(root){

   open fun bind(item: MediaItem, position: Int, selectedPosition: Int) = Unit

   open fun bind(item: PlaylistUi) = Unit

}

class PlaylistTopBarViewHolder(
    context: Context,
    private val  binding: PlaylistDetailsTopbarBinding,
    imageLoader: ImageLoader,
    cacheStrategy: DiskCacheStrategy,
): AdapterViewHolder(binding.root){

    private val mapper = PlaylistUi.ShowToUiPlaylistDetails(binding,imageLoader,context,cacheStrategy)

    override fun bind(item: PlaylistUi) {
        item.map(mapper)
    }

}