package com.example.musicapp.musicdialog.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.UnstableApi
import androidx.navigation.fragment.findNavController
import com.example.musicapp.R
import com.example.musicapp.favorites.presentation.FavoritesViewModel
import com.example.musicapp.main.di.App
import com.example.musicapp.main.di.AppComponent
import javax.inject.Inject


@UnstableApi class MusicDialogFragment : DialogFragment() {

    private lateinit var viewModel: MusicDialogViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var appCoponent: AppComponent

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        appCoponent = (context?.applicationContext as App).appComponent
        appCoponent.inject(this)
        viewModel = ViewModelProvider(this, factory)[MusicDialogViewModel::class.java]

        val listener = DialogInterface.OnClickListener { p0, buttonId ->
            if(buttonId == DialogInterface.BUTTON_POSITIVE)
                viewModel.saveTrack(viewModel.fetchData()!!)
            else viewModel.notSave()
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