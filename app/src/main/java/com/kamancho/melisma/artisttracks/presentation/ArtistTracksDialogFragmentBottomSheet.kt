package com.kamancho.melisma.artisttracks.presentation

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.Logger
import com.kamancho.melisma.artisttracks.di.ArtistsTracksComponent
import com.kamancho.melisma.databinding.ArtistsTracksDialogFragmentBinding
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.main.presentation.PlayerCommunicationState
import com.kamancho.melisma.searchhistory.presentation.ViewPagerFragmentsAdapter
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
class ArtistTracksDialogFragmentBottomSheet :
    BottomSheetDialogFragment(R.layout.artists_tracks_dialog_fragment) {

    private val binding by viewBinding(ArtistsTracksDialogFragmentBinding::bind)

    private lateinit var viewModel: ArtistsTracksViewModel

    private lateinit var component: ArtistsTracksComponent

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var viewPagerCallback: ViewPager2.OnPageChangeCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        component = (context.applicationContext as App).appComponent
            .playerComponent().build().artistsTracksComponent().build()
        component.inject(this)

        viewModel = ViewModelProvider(this, factory)[ArtistsTracksViewModel::class.java]

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.saveTrackId(requireArguments().getString(TRACK_ID_KEY))
        Logger.logFragment(
            findNavController().currentDestination?.label.toString(),
            requireContext()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.layoutParams.apply {
            height = resources.displayMetrics.heightPixels
        }

        BottomSheetBehavior.from(view.parent as View).apply {
            skipCollapsed = true
            peekHeight = 0
            isFitToContents = false
            state = BottomSheetBehavior.STATE_EXPANDED
        }




        val artistTracksFragments = mutableListOf<ArtistTracksListFragment>()

        val artists = requireArguments().getStringArrayList(ARTISTS_KEY)
        val artistsIds = requireArguments().getStringArrayList(ARTISTS_IDS_KEY)

        artists?.forEachIndexed { index, _ ->
            val id = artistsIds?.getOrNull(index)
            artistTracksFragments.add(
                ArtistTracksListFragment.newInstance(
                    id ?: index.toString(),
                    id !=null
                )
            )
            Log.d("forEachIndexed", "onViewCreated: ${artistsIds?.getOrNull(index)} ")
        }

        val adapter = ViewPagerFragmentsAdapter(
            childFragmentManager,
            lifecycle,
            artistTracksFragments
        )

        binding.artistTracksViewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.artistTracksViewPager) { tab, position ->
            tab.text = artists?.getOrNull(position)?: return@TabLayoutMediator
        }.attach()

        viewPagerCallback = object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.changePage(artistsIds?.getOrNull(position) ?: position.toString())
            }

        }

        binding.artistTracksViewPager.registerOnPageChangeCallback(viewPagerCallback!!)

        lifecycleScope.launch {
            viewModel.collectPlayerControls(this@ArtistTracksDialogFragmentBottomSheet){
                it.apply(binding.bottomPlayer, imageLoader,viewModel)
            }
        }

        lifecycleScope.launch {
            viewModel.collectGlobalSingleUiEventCommunication(this@ArtistTracksDialogFragmentBottomSheet){
                it.applyForBottomSheet(
                    dialog?.window!!.decorView,
                    requireContext(),
                    binding.bottomPlayer.bottomPlayerBar
                )
            }
        }

        binding.bottomPlayer.playBtn.setOnClickListener {
            if ((it as ToggleButton).isChecked)
                viewModel.playerAction(PlayerCommunicationState.Pause)
            else
                viewModel.playerAction(PlayerCommunicationState.Resume)
        }

        binding.bottomPlayer.previousBtn.setOnClickListener {
            viewModel.playerAction(PlayerCommunicationState.Previous)
        }

        binding.bottomPlayer.nextBtn.setOnClickListener {
            viewModel.playerAction(PlayerCommunicationState.Next)
        }


        binding.backBtn.setOnClickListener {
           dismiss()
        }


    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.returnPreviousPageTracks()
        super.onDismiss(dialog)
    }

    override fun onPause() {
        super.onPause()
        dialog?.window?.setWindowAnimations(-1)
    }

    override fun onDestroyView() {
        binding.artistTracksViewPager.unregisterOnPageChangeCallback(viewPagerCallback!!)
        super.onDestroyView()
    }

    companion object {
        private const val ARTISTS_KEY: String = "ARTISTS_KEY"
        private const val ARTISTS_IDS_KEY: String = "ARTISTS_IDS_KEY"
        private const val TRACK_ID_KEY: String = "TRACK_ID_KEY"

        fun newInstance(
          artists: List<String>,
          artistsIds: List<String>,
          trackId: String
        ): ArtistTracksDialogFragmentBottomSheet {
            val bundle = Bundle()
            bundle.putStringArrayList(ARTISTS_KEY, ArrayList(artists))
            bundle.putStringArrayList(ARTISTS_IDS_KEY, ArrayList(artistsIds))
            bundle.putString(TRACK_ID_KEY, trackId)
            val fragment = ArtistTracksDialogFragmentBottomSheet()
            fragment.arguments = bundle
            return fragment
        }
    }
}