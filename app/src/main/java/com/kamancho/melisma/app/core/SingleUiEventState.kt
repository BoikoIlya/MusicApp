package com.kamancho.melisma.app.core

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.kamancho.melisma.R
import com.kamancho.melisma.databinding.ActivityMainBinding
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

    fun applyForBottomSheet(
        decorView: View,
        context: Context,
        anchorView: View? = null
        )

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
                    .setAnchorView(if (bottomPlayer.bottomPlayerBar.isVisible)bottomPlayer.bottomPlayerBar else bottomNavView)
                    .show()
        }

        override fun applyForBottomSheet(
            decorView: View,
            context: Context,
            anchorView: View?
        ) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(decorView.windowToken,0)

            val snackBar = Snackbar.make(decorView, message, Snackbar.LENGTH_SHORT)
            val snackBarView: View = snackBar.view
            snackBarView.background =
                AppCompatResources.getDrawable(context, R.drawable.rounded_corners_shape)
            snackBar
                .setBackgroundTint(context.getColor(bgColorId))
                .setTextColor(context.getColor(R.color.white))
                .setAnchorView(anchorView)
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

        override fun applyForBottomSheet(
            decorView: View,
            context: Context,
            anchorView: View?
        ) = Unit

    }

    data class ShowFragment(
        private val fragment: Fragment
    ): SingleUiEventState {
        override fun apply(
            fragmentManager: FragmentManager,
            context: Context,
            binding: ActivityMainBinding,
        ) {
            fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .addToBackStack(fragment.tag)
                .commit()
        }

        override fun applyForBottomSheet(
            decorView: View,
            context: Context,
            anchorView: View?
        ) = Unit
    }

}


