package com.kamancho.melisma.vkauth.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.databinding.ActivityMainBinding
import com.kamancho.melisma.main.presentation.MainActivity

/**
 * Created by HP on 15.09.2023.
 **/
sealed interface SingleAuthState{

    fun apply(
        viewModel: AuthViewModel,
        navController: NavController,
        context: FragmentActivity
        )

    data class LaunchRedirection(
        private val url: String
    ): SingleAuthState {

        companion object{
             val redirection_url = "redirectionUrl"
        }

        override fun apply(
            viewModel: AuthViewModel,
            navController: NavController,
            context: FragmentActivity
        ) {
            val bundle = Bundle()
            bundle.putString(redirection_url,url)
            navController.navigate(R.id.action_authFragment_to_authWebViewFragment,bundle)
        }
    }

    object LaunchMainActivity: SingleAuthState{
        override fun apply(
            viewModel: AuthViewModel,
            navController: NavController,
            context: FragmentActivity
        ) {
            context.startActivity(Intent(context, MainActivity::class.java))
            context.finish()
        }

    }

    data class ShowDialog(
        private val dialog: DialogFragment,
    ) : SingleAuthState {

        override fun apply(
            viewModel: AuthViewModel,
            navController: NavController,
            context: FragmentActivity
        ) {
            dialog.show(context.supportFragmentManager, dialog.tag)
        }


    }
}