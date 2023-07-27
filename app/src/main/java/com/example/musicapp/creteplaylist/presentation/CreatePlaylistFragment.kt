package com.example.musicapp.creteplaylist.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.musicapp.R
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.creteplaylist.di.PlaylistDataComponent
import com.example.musicapp.main.di.App
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/
class CreatePlaylistFragment: PlaylistDataFragment(R.layout.playlist_data_fragment) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var component: PlaylistDataComponent

    private lateinit var viewModel: CreatePlaylistViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component = (context.applicationContext as App).appComponent
            .playlistDataComponent().build()
        component.inject(this)

        viewModel = ViewModelProvider(this, factory)[CreatePlaylistViewModel::class.java]
        baseViewModel = viewModel
        baseImageLoader = imageLoader
    }

    override fun titleTextChanged(title: String) {
        viewModel.verifyData(title)
    }

    override fun additionalActionOnRecyclerUpdate() = Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.addTracksBtn.setOnClickListener {
            findNavController().navigate(R.id.action_createPlaylistFragment_to_addToPlaylistFragment)
        }
        super.onViewCreated(view, savedInstanceState)

    }
}