package com.example.musicapp.player.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.musicapp.R
import com.example.musicapp.main.di.App
import com.example.musicapp.searchhistory.di.SearchHistoryComponent
import com.example.musicapp.searchhistory.presentation.ClearSearchHistoryDialogViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 02.07.2023.
 **/
class DeleteTrackFromPlayerMenuDialog: DialogFragment() {

    private lateinit var viewModel: DeleteTrackFromPlayerMenuDialogViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var currentTrackId = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        (context?.applicationContext as App).appComponent.playerComponent().build()
        .inject(this)
        viewModel = ViewModelProvider(requireActivity(), factory)[DeleteTrackFromPlayerMenuDialogViewModel::class.java]

        val clickListener = DialogInterface.OnClickListener { _, buttonId ->
            if(buttonId == DialogInterface.BUTTON_POSITIVE)  viewModel.deleteTrackFromFavorites()
        }

        val dialog= AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setMessage(R.string.delete_track_player_menu_confirm_message)
            .setPositiveButton(R.string.yes_btn_text,clickListener)
            .setNegativeButton(R.string.no_btn_text,clickListener)
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
        return dialog
    }


}