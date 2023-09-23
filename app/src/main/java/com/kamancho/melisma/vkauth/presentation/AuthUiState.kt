package com.kamancho.melisma.vkauth.presentation

import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.kamancho.melisma.databinding.ActivityAuthBinding
import com.kamancho.melisma.databinding.AuthFragmentBinding
import com.kamancho.melisma.main.presentation.MainActivity

/**
 * Created by HP on 17.06.2023.
 **/
sealed interface AuthUiState{

    fun apply(
        binding: AuthFragmentBinding,
        context: FragmentActivity
    )

    object Empty: AuthUiState{
        override fun apply( binding: AuthFragmentBinding,
                            context: FragmentActivity) = Unit
    }

    object Loading: AuthUiState{

        override fun apply(
            binding: AuthFragmentBinding,
            context: FragmentActivity
        )= with(binding) {
            errorTvAuth.visibility = View.GONE
            authProgress.visibility = View.VISIBLE
            loginEdt.isEnabled = false
            passwordEdt.isEnabled = false
            loginBtn.visibility = View.GONE
        }

    }

    data class Error(
        private val message: String
    ): AuthUiState{
        override fun apply(
            binding: AuthFragmentBinding,
            context: FragmentActivity
        ) = with(binding) {
            authProgress.visibility = View.GONE
            errorTvAuth.text = message
            errorTvAuth.visibility = View.VISIBLE
            loginEdt.isEnabled = true
            passwordEdt.isEnabled = true
            loginBtn.visibility = View.VISIBLE
        }

    }

    object HideError: AuthUiState{
        override fun apply(binding: AuthFragmentBinding, context: FragmentActivity) = with(binding) {
            authProgress.visibility = View.GONE
            errorTvAuth.text = ""
            errorTvAuth.visibility = View.GONE
            loginEdt.isEnabled = true
            passwordEdt.isEnabled = true
            loginBtn.visibility = View.VISIBLE
        }

    }



}