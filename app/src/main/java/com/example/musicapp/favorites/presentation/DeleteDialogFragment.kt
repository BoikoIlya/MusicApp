package com.example.musicapp.favorites.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.R
import com.example.musicapp.favorites.di.FavoriteComponent
import com.example.musicapp.main.di.App
import javax.inject.Inject

/**
 * Created by HP on 26.06.2023.
 **/
class DeleteDialogFragment : DialogFragment() {

    private lateinit var viewModel: DeleteDialogViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        (context?.applicationContext as App).appComponent.favoriteComponent().build()
            .inject(this)
        viewModel = ViewModelProvider(requireActivity(), factory)[DeleteDialogViewModel::class.java]

        val clickListener = DialogInterface.OnClickListener { _, buttonId ->
            when(buttonId){
                DialogInterface.BUTTON_POSITIVE -> viewModel.removeTrack()
                DialogInterface.BUTTON_NEGATIVE -> viewModel.resetSwipedItem()
            }
        }

        val dialog= AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setMessage(R.string.delete_track_confirm_message)
            .setPositiveButton(R.string.yes_btn_text,clickListener)
            .setNegativeButton(R.string.no_btn_text,clickListener)
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
        return dialog
    }


    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.resetSwipedItem()
    }

}