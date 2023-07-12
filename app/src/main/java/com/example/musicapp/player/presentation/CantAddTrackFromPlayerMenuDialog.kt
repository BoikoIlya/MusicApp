package com.example.musicapp.player.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.musicapp.R

/**
 * Created by HP on 04.07.2023.
 **/
class CantAddTrackFromPlayerMenuDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(R.string.cant_re_add)
            .setTitle(R.string.warning_title)
            .setPositiveButton(R.string.ok) { p0, p1 -> }
            .show()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
        return dialog
    }
}