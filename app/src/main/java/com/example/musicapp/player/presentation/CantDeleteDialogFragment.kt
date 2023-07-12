package com.example.musicapp.player.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.musicapp.R

/**
 * Created by HP on 05.07.2023.
 **/
class CantDeleteDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(R.string.cant_delete)
            .setTitle(R.string.warning_title)
            .setPositiveButton(R.string.ok) { p0, p1 -> }
            .show()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
        return dialog
    }
}