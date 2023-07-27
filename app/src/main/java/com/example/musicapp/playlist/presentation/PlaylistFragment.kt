package com.example.musicapp.playlist.presentation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Selector
import com.example.musicapp.databinding.FragmentPlaylistBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.playlist.di.PlaylistComponent
import com.example.musicapp.trending.presentation.TracksAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


 class PlaylistFragment : Fragment(R.layout.fragment_playlist) {

    private val binding by viewBinding(FragmentPlaylistBinding::bind)

    private lateinit var viewModel: PlaylistViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var playlistComponent: PlaylistComponent


    override fun onAttach(context: Context) {
        super.onAttach(context)
        playlistComponent = (context.applicationContext as App).appComponent.playlistComponent().build()
        playlistComponent.inject(this)

        viewModel = ViewModelProvider(this, factory)[PlaylistViewModel::class.java]

    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.collectState(this@PlaylistFragment){
                it.apply(binding)
            }
        }

        binding.errorLayout.reloadBtn.setOnClickListener {
            viewModel.loadData()
        }


        binding.playlistRcv.layoutManager = LinearLayoutManager(requireContext())

        val tracksAdapter = TracksAdapter(
            this.requireContext(),
            playClickListener = object: Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    viewModel.playMusic(data)
                }
            }, saveClickListener = object : ClickListener<MediaItem> {
                override fun onClick(data: MediaItem) {
                    viewModel.checkAndAddTrackToFavorites(data)
                }
            }, imageLoader)

        binding.playlistRcv.adapter = tracksAdapter
        (binding.playlistRcv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        lifecycleScope.launch{
            viewModel.collectAdditionalPlaylistInfo(this@PlaylistFragment){
                binding.albumName.text = it.first
                imageLoader.loadImage(it.second,binding.playlistImg)
                binding.description.text = it.third
            }
        }

        lifecycleScope.launch{
            viewModel.collectData(this@PlaylistFragment){
                tracksAdapter.map(it)
                viewModel.saveCurrentPageQueue(it)
                binding.trackAmount.text = "${it.size} ${getString(R.string.tracks)}"
            }
        }

        lifecycleScope.launch{
            viewModel.collectSelectedTrack(this@PlaylistFragment){
                tracksAdapter.newPosition(it)
            }
        }

//        lifecycleScope.launch {
//            viewModel.collectPlayerControls(this@PlaylistFragment){
//                it.apply(binding.playlistRcv)
//            }
//        }

        binding.backBtnQueue.setOnClickListener {
            findNavController().popBackStack()
        }

//        binding.shuffleBtn.setOnClickListener {
//            viewModel.shuffle()
//        }

        binding.description.setOnClickListener {
           if (binding.description.layout.getEllipsisCount(binding.description.lineCount - 1) <= 0)
               return@setOnClickListener
           val dialog = AlertDialog.Builder(requireContext())
                .setMessage(binding.description.text.toString())
                .create()

            dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
            dialog.show()
        }
    }

}