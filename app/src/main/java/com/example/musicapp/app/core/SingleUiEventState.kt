package com.example.musicapp.app.core

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


/**
 * Created by HP on 08.04.2023.
 **/
sealed interface SingleUiEventState{


    fun apply(
        fragmentManager: FragmentManager,
        context: Context,
        binding: ActivityMainBinding,
    )

    fun applyForBottomSheet(decorView: View,context: Context,)

    abstract class ShowSnackBar(
        private val message: String,
        private val bgColorId: Int,
    ): SingleUiEventState {

        override fun apply(
            fragmentManager: FragmentManager,
            context: Context,
            binding: ActivityMainBinding,
        ) = with(binding) {

                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

                val snackBar = Snackbar.make(fragmentContainer, message, Snackbar.LENGTH_SHORT)
                val snackBarView: View = snackBar.view
                snackBarView.background =
                    AppCompatResources.getDrawable(context, R.drawable.rounded_corners_shape)
                snackBar
                    .setBackgroundTint(context.getColor(bgColorId))
                    .setTextColor(context.getColor(R.color.white))
                    .setAnchorView(if (bottomPlayerBar.isVisible) bottomPlayerBar else bottomNavView)
                    .show()
        }

        override fun applyForBottomSheet(decorView: View,context: Context,) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(decorView.windowToken,0)

            val snackBar = Snackbar.make(decorView, message, Snackbar.LENGTH_SHORT)
            val snackBarView: View = snackBar.view
            snackBarView.background =
                AppCompatResources.getDrawable(context, R.drawable.rounded_corners_shape)
            snackBar
                .setBackgroundTint(context.getColor(bgColorId))
                .setTextColor(context.getColor(R.color.white))
                .show()
        }


        data class Error(
            private val message: String,
        ) : ShowSnackBar(message, R.color.red)

        data class Success(
            private val message: String,
        ) : ShowSnackBar(message, R.color.green)
    }




    data class ShowDialog(
        private val dialog: DialogFragment,
    ) : SingleUiEventState{

        override fun apply(
            fragmentManager: FragmentManager,
            context: Context,
            binding: ActivityMainBinding,
        ) {
            dialog.show(fragmentManager, dialog.tag)
        }

        override fun applyForBottomSheet(decorView: View,context: Context,) = Unit

    }


}


