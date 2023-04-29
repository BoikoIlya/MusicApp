package com.example.musicapp.updatesystem.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.marginStart
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.musicapp.R
import com.example.musicapp.main.di.App
import com.example.musicapp.main.di.AppComponent
import com.example.musicapp.musicdialog.presentation.MusicDialogViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.launch
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
        Log.d("tag", "apply: CRETE DIALOG")
        appCoponent = (context?.applicationContext as App).appComponent
        appCoponent.inject(this)
        viewModel = ViewModelProvider(this, factory)[UpdateDialogViewModel::class.java]
        val listener = DialogInterface.OnClickListener { p0, buttonId ->
            if(buttonId == DialogInterface.BUTTON_POSITIVE) {
                viewModel.loadUpdate()
               val progressDialog = AlertDialog.Builder(requireContext())
                   .setView(ProgressBar(requireContext()))
                    .setCancelable(false)
                    .setMessage(requireContext().getString(R.string.load_update))
                    .create()
                progressDialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
                progressDialog.show()
            }else{
                viewModel.dismiss()
            }
        }
        val dialog= AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setMessage(viewModel.readDialogMessage())
            .setPositiveButton(R.string.update_btn_text,listener)
            .setNegativeButton(R.string.later_btn_text,listener)
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
        return dialog
    }


}