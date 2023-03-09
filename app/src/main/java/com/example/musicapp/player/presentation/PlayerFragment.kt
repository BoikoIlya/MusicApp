package com.example.musicapp.player.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.PlayerFragmentBinding

/**
 * Created by HP on 25.01.2023.
 **/
class PlayerFragment:Fragment(R.layout.player_fragment) {

    lateinit var binding: PlayerFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlayerFragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(binding.songImg)
            .load("https://direct.napster.com/imageserver/v2/playlists/pp.224772103/artists/images/230x153.jpg")
            .thumbnail(Glide.with(binding.songImg).asDrawable().sizeMultiplier(0.05f))
            .into(binding.songImg)
    }
}