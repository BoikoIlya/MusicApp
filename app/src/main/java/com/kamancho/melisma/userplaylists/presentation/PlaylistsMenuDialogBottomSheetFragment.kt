package com.kamancho.melisma.userplaylists.presentation

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kamancho.melisma.R
import com.kamancho.melisma.databinding.PlaylistMenuDialogBottomSheetBinding
import com.kamancho.melisma.main.di.App
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kamancho.melisma.userplaylists.presentation.FavoritesPlaylistsFragment.Companion.playlist_key
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 14.07.2023.
 **/
class PlaylistsMenuDialogBottomSheetFragment: BottomSheetDialogFragment(R.layout.playlist_menu_dialog_bottom_sheet) {

    private lateinit var viewModel: PlaylistsMenuDialogBottomSheetViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val binding by viewBinding(PlaylistMenuDialogBottomSheetBinding::bind)

    private val args: PlaylistsMenuDialogBottomSheetFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireContext().applicationContext as App).appComponent.playlistsComponent().build()
            .inject(this)

        viewModel = ViewModelProvider(
            requireActivity(),
            factory
        )[PlaylistsMenuDialogBottomSheetViewModel::class.java]
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.checkIfCanEdit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch{
            viewModel.collectCanEditCommunication(this@PlaylistsMenuDialogBottomSheetFragment){
                binding.editOption.visibility = it
            }
        }

        binding.deleteOption.setOnClickListener {
            viewModel.launchDeletePlaylistDialog()
            findNavController().popBackStack()
        }

        binding.editOption.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(playlist_key,args.playlistItem)
            findNavController().navigate(R.id.action_playlistsMenuDialogBottomSheetFragment_to_editPlaylistFragment,bundle)
        }
    }


    override fun onPause() {
        super.onPause()
        dialog?.window?.setWindowAnimations(-1)
    }
}