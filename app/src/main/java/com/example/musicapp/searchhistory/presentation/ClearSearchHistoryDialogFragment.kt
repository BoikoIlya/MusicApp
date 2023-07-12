package com.example.musicapp.searchhistory.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.R
import com.example.musicapp.favorites.di.FavoriteComponent
import com.example.musicapp.favorites.presentation.DeleteDialogViewModel
import com.example.musicapp.main.di.App
import com.example.musicapp.searchhistory.di.SearchHistoryComponent
import javax.inject.Inject

/**
 * Created by HP on 30.06.2023.
 **/
class ClearSearchHistoryDialogFragment: DialogFragment() {

    private lateinit var viewModel: ClearSearchHistoryDialogViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var appCoponent: SearchHistoryComponent

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        appCoponent = (context?.applicationContext as App).appComponent.searchHistoryComponent().build()
        appCoponent.inject(this)
        viewModel = ViewModelProvider(requireActivity(), factory)[ClearSearchHistoryDialogViewModel::class.java]

        val clickListener = DialogInterface.OnClickListener { _, buttonId ->
               if(buttonId == DialogInterface.BUTTON_POSITIVE)  viewModel.clearHistory()
            }

        val dialog= AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setMessage(R.string.clear_search_history_confirm_message)
            .setPositiveButton(R.string.yes_btn_text,clickListener)
            .setNegativeButton(R.string.no_btn_text,clickListener)
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
        return dialog
    }
}