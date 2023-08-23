package com.kamancho.melisma.addtoplaylist.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.databinding.TrackItemBinding


/**
 * Created by HP on 16.07.2023.
 **/
open class SelectedTracksAdapter(
   private val context: Context,
   private val clickListener: ClickListener<SelectedTrackUi>,
   private val imageLoader: ImageLoader,
): RecyclerView.Adapter<SelectedTracksViewHolder>(), Mapper<List<SelectedTrackUi>,Unit> {


    private val selectedTracks = mutableListOf<SelectedTrackUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedTracksViewHolder {
        return SelectedTracksViewHolder(
            context,
            TrackItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), clickListener,imageLoader
        )
    }

    override fun getItemCount(): Int = selectedTracks.size

    override fun onBindViewHolder(holder: SelectedTracksViewHolder, position: Int) {
        holder.bind(selectedTracks[position])
    }

    override fun map(data: List<SelectedTrackUi>) {
        val differ = DiffUtil.calculateDiff(SelectedTracksUiDiffUtilCallback(data,selectedTracks))
        selectedTracks.clear()
        selectedTracks.addAll(data)
        differ.dispatchUpdatesTo(this)
    }
}

open class SelectedTracksViewHolder(
    private val context: Context,
    private val binding: TrackItemBinding,
    private val clickListener: ClickListener<SelectedTrackUi>,
    private val imageLoader: ImageLoader,
): ViewHolder(binding.root){
    private val mapper = SelectedTrackUi.ShowToUi(binding, imageLoader,context)

    open fun bind(item: SelectedTrackUi) {
        item.map(mapper)
         binding.root.setOnClickListener {
             clickListener.onClick(item)
         }
    }
}

class SelectedTracksUiDiffUtilCallback(
    private val newList: List<SelectedTrackUi>,
    private val oldList: List<SelectedTrackUi>,
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].map(oldList[oldItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition] == oldList[oldItemPosition]
    }
}