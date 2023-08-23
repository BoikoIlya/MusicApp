package com.kamancho.melisma.userplaylists.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.kamancho.melisma.R
import com.kamancho.melisma.main.di.App
import javax.inject.Inject

/**
 * Created by HP on 20.07.2023.
 **/
class DeletePlaylistDialogFragment: DialogFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: PlaylistsMenuDialogBottomSheetViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        (context?.applicationContext as App).appComponent.playlistsComponent().build()
            .inject(this)
        viewModel = ViewModelProvider(requireActivity(), factory)[PlaylistsMenuDialogBottomSheetViewModel::class.java]

        val clickListener = DialogInterface.OnClickListener { _, buttonId ->
            if(buttonId==DialogInterface.BUTTON_POSITIVE)
                viewModel.deletePlaylist()
        }

        val dialog= AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setMessage(R.string.delete_playlist_message)
            .setPositiveButton(R.string.yes_btn_text,clickListener)
            .setNegativeButton(R.string.no_btn_text,clickListener)
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
        return dialog
    }
}