package com.kamancho.melisma.settings.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.kamancho.melisma.R
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.main.di.AppComponent
import javax.inject.Inject

/**
 * Created by HP on 23.08.2023.
 **/
class DeleteDownloadedTracksDialogFragment: DialogFragment() {

    private lateinit var viewModel: SettingsViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var appCoponent: AppComponent

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        appCoponent = (context?.applicationContext as App).appComponent
        appCoponent.inject(this)
        viewModel = ViewModelProvider(requireActivity(), factory)[SettingsViewModel::class.java]

        val listener = DialogInterface.OnClickListener { _, buttonId ->
            if(buttonId == DialogInterface.BUTTON_POSITIVE)
                viewModel.deleteDownloadedTracks()
        }
        val dialog= AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setMessage(R.string.delete_downloaded_dialog_message)
            .setPositiveButton(R.string.yes_btn_text,listener)
            .setNegativeButton(R.string.no_btn_text,listener)
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
        return dialog
    }
}