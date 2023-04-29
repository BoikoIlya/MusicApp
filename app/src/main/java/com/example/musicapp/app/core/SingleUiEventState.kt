package com.example.musicapp.app.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
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

    abstract class ShowSnackBar(
        private val message: String,
        private val bgColorId: Int
    ): SingleUiEventState {

        override fun apply(
            fragmentManager: FragmentManager,
            context: Context,
            binding: ActivityMainBinding,
        ) = with(binding) {
            val snackBar = Snackbar.make(fragmentContainer, message, Snackbar.LENGTH_SHORT)
            val snackBarView: View = snackBar.view
            snackBarView.background =
                AppCompatResources.getDrawable(context, R.drawable.rounded_corners_shape)
            snackBar
                .setBackgroundTint(context.getColor(bgColorId))
                .setTextColor(context.getColor(R.color.white))
                .setAnchorView(if(bottomPlayerBar.isVisible) bottomPlayerBar else bottomNavView)
                .show()

        }


        data class Error(
            private val message: String
        ) : ShowSnackBar(message, R.color.red)

        data class Success(
            private val message: String
        ) : ShowSnackBar(message, R.color.green)
    }

}

sealed interface UiEventState{


    fun apply(
        fragmentManager: FragmentManager,
        context: Context,
        binding: ActivityMainBinding,
    )

    @UnstableApi
    data class ShowDialog(
        private val dialog: DialogFragment
    ) : UiEventState{

        override fun apply(
            fragmentManager: FragmentManager,
            context: Context,
            binding: ActivityMainBinding,
        ) {
            dialog.show(fragmentManager, dialog.tag)
        }
    }



    object ClearCommunication: UiEventState{
        override fun apply(
            fragmentManager: FragmentManager,
            context: Context,
            binding: ActivityMainBinding,
        ) = Unit

    }

    data class LoadUpdate(
        private val apkUrl: String
    ) : UiEventState{


        override fun apply(
            fragmentManager: FragmentManager,
            context: Context,
            binding: ActivityMainBinding,
        ) {

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(apkUrl)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

    }
}

