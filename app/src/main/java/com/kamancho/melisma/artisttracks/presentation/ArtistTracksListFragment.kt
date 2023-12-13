package com.kamancho.melisma.artisttracks.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.Selector
import com.kamancho.melisma.artisttracks.di.ArtistsTracksComponent
import com.kamancho.melisma.databinding.ArtistTracksListFragmentBinding
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.trending.presentation.TracksAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
class ArtistTracksListFragment : Fragment(R.layout.artist_tracks_list_fragment) {

    private val binding by viewBinding(ArtistTracksListFragmentBinding::bind)

    private lateinit var viewModel: ArtistTracksListViewModel

    private lateinit var component: ArtistsTracksComponent

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component = (context.applicationContext as App).appComponent
            .playerComponent().build().artistsTracksComponent().build()
        component.inject(this)

        viewModel = ViewModelProvider(this, factory)[ArtistTracksListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadTracks(
            requireArguments().getString(ARTIST_ID_KEY),
            requireArguments().getBoolean(IS_ID_EXIST_KEY),
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.artistTracksRcv.layoutManager = LinearLayoutManager(requireContext())

        val adapter = TracksAdapter(
            context = requireContext(),
            saveClickListener = object: ClickListener<MediaItem> {
                override fun onClick(data: MediaItem) {
                    viewModel.checkAndAddTrackToFavorites(data)
                }
            },
            playClickListener = object : Selector<MediaItem> {
                override fun onSelect(data: MediaItem, position: Int) {
                    viewModel.playMusic(data)
                }
            },
            imageLoader = imageLoader,
            addBtnVisibility = View.VISIBLE,
            cacheStrategy = DiskCacheStrategy.AUTOMATIC,
            layoutManager =  binding.artistTracksRcv.layoutManager as RecyclerView.LayoutManager
        )

        binding.artistTracksRcv.adapter = adapter
        (binding.artistTracksRcv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false


        lifecycleScope.launch {
            viewModel.collectData(this@ArtistTracksListFragment){
                Log.d("ArtistTracksListFragment", "${it.size} ")
                adapter.map(it)
                viewModel.saveCurrentPageQueue(it)
            }
        }

        lifecycleScope.launch {
            viewModel.collectState(this@ArtistTracksListFragment){
                Log.d("ArtistTracksListFragment", "${it} ")
                it.apply(binding)
            }
        }

        lifecycleScope.launch {
            viewModel.collectSelectedTrack(this@ArtistTracksListFragment){
                adapter.newPosition(it)
            }
        }

        lifecycleScope.launch {
            viewModel.collectPageChangerCommunication(this@ArtistTracksListFragment){
                Log.d("collectPageChangerCommunication", "out $it  ${requireArguments().getString(ARTIST_ID_KEY)}")
                if(it==requireArguments().getString(ARTIST_ID_KEY)) {
                    Log.d("collectPageChangerCommunication",  "onViewCreated: save $it  count ${adapter.itemCount}  ")
                    adapter.saveCurrPageTracks(viewModel)
                }
            }
        }

        binding.reloadBtn.setOnClickListener {
            viewModel.reloadTracks(
                requireArguments().getString(ARTIST_ID_KEY),
                requireArguments().getBoolean(IS_ID_EXIST_KEY)
            )
        }
    }

    companion object {
        private const val ARTIST_ID_KEY: String = "ARTIST_ID_KEY"
        private const val IS_ID_EXIST_KEY: String = "IS_ID_EXIST"

        fun newInstance(artistId: String, isIdExist: Boolean = true): ArtistTracksListFragment {
            val bundle = Bundle()
            bundle.putString(ARTIST_ID_KEY, artistId)
            bundle.putBoolean(IS_ID_EXIST_KEY, isIdExist)
            val fragment = ArtistTracksListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}