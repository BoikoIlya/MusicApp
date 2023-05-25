package com.example.musicapp.updatesystem.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.R
import com.example.musicapp.main.di.App
import com.example.musicapp.main.di.AppComponent
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/
class UpdateDialogFragment: DialogFragment() {

    private lateinit var viewModel: UpdateDialogViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var appCoponent: AppComponent


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        appCoponent = (context?.applicationContext as App).appComponent
        appCoponent.inject(this)
        viewModel = ViewModelProvider(this, factory)[UpdateDialogViewModel::class.java]
        val data = viewModel.readDialogMessageAndUrl()
        val listener = DialogInterface.OnClickListener { _, buttonId ->
            if(buttonId == DialogInterface.BUTTON_POSITIVE)
                viewModel.loadUpdate(data?.second?:"")
        }
        val dialog= AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setMessage(data?.first)
            .setPositiveButton(R.string.update_btn_text,listener)
            .setNegativeButton(R.string.later_btn_text,listener)
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
        return dialog
    }


}