package com.example.musicapp.creteplaylist.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.musicapp.R
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.addtoplaylist.presentation.SelectedTracksAdapter
import com.example.musicapp.addtoplaylist.presentation.SelectedTracksViewHolder
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.databinding.TrackItemBinding

/**
 * Created by HP on 16.07.2023.
 **/
class PlaylistDataSelectedTracksAdapter(
   private val context: Context,
   private val clickListener: ClickListener<SelectedTrackUi>,
   private val imageLoader: ImageLoader,
   private val btnVisibility: Int
): SelectedTracksAdapter(
    context, clickListener,imageLoader
) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistDataSelectedTracksViewHolder {
        return PlaylistDataSelectedTracksViewHolder(
            context,
            TrackItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), clickListener,imageLoader,btnVisibility
        )
    }
}

class PlaylistDataSelectedTracksViewHolder(
    context: Context,
    private val binding: TrackItemBinding,
    private val deleteClickListener: ClickListener<SelectedTrackUi>,
    imageLoader: ImageLoader,
    private val btnVisibility: Int
): SelectedTracksViewHolder(context,binding,deleteClickListener,imageLoader){



    override fun bind(item: SelectedTrackUi) {
        super.bind(item)
        binding.addBtn.apply {
            setImageResource(R.drawable.bin)
            imageTintList = ContextCompat.getColorStateList(context,R.color.black)
            visibility = btnVisibility
            setOnClickListener {
                deleteClickListener.onClick(item)
            }
        }
        binding.root.setOnClickListener(null)
    }
}