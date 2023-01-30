package com.example.musicapp.trending.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.musicapp.ClickListener
import com.example.musicapp.ImageLoader
import com.example.musicapp.app.Mapper
import com.example.musicapp.databinding.SongItemBinding

/**
 * Created by HP on 30.01.2023.
 **/
class TrendingTracksAdapter(
    private val playClickListener: ClickListener<TrackUi>,
    private val saveClickListener: ClickListener<TrackUi>,
): RecyclerView.Adapter<TrendingTracksViewHolder>(), Mapper<List<TrackUi>,Unit> {

    private val tracks = mutableListOf<TrackUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingTracksViewHolder {
        return TrendingTracksViewHolder(
            SongItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), playClickListener, saveClickListener
        )
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrendingTracksViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun map(data: List<TrackUi>) {
        val diff = TendingTracksDiffUtilCallback(data, tracks)
        val result = DiffUtil.calculateDiff(diff)
        tracks.clear()
        tracks.addAll(data)
        result.dispatchUpdatesTo(this)
    }
}

class TrendingTracksViewHolder(
    private val binding: SongItemBinding,
    private val playClickListener: ClickListener<TrackUi>,
    private val saveClickListener: ClickListener<TrackUi>,
): ViewHolder(binding.root){

    private val mapper = TrackUi.ListItemUi(binding)

    fun bind(item: TrackUi){
        item.map(mapper)

        binding.root.setOnClickListener {
            playClickListener.onClick(item)
        }

        binding.addBtn.setOnClickListener {
            saveClickListener.onClick(item)
        }
    }

}

class TendingTracksDiffUtilCallback(
    private val newList: List<TrackUi>,
    private val oldList: List<TrackUi>
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