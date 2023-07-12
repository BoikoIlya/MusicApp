package com.example.musicapp.musicdialog.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.main.di.App
import com.example.musicapp.main.di.AppComponent
import javax.inject.Inject


@UnstableApi class AddTrackDialogFragment : DialogFragment() {

    private lateinit var viewModel: AddTrackDialogViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var appCoponent: AppComponent

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        appCoponent = (context?.applicationContext as App).appComponent
        appCoponent.inject(this)
        viewModel = ViewModelProvider(requireActivity(), factory)[AddTrackDialogViewModel::class.java]

        val listener = DialogInterface.OnClickListener { _, buttonId ->
            if(buttonId == DialogInterface.BUTTON_POSITIVE)
                viewModel.saveTrack()
        }
        val dialog= AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setMessage(R.string.dublicated_track_message)
            .setPositiveButton(R.string.yes_btn_text,listener)
            .setNegativeButton(R.string.no_btn_text,listener)
            .create()

            dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
        return dialog
    }


}