package com.example.musicapp.vkauth.presentation

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.example.musicapp.R
import com.example.musicapp.databinding.ActivityAuthBinding
import com.example.musicapp.favorites.presentation.FavoritesFragment
import com.example.musicapp.main.presentation.MainActivity

/**
 * Created by HP on 17.06.2023.
 **/
sealed interface AuthUiState{

    fun apply(
        binding: ActivityAuthBinding,
        context: AuthActivity
    )

    object Empty: AuthUiState{
        override fun apply( binding: ActivityAuthBinding,
                            context: AuthActivity) = Unit
    }

    object Loading: AuthUiState{

        override fun apply(
            binding: ActivityAuthBinding,
            context: AuthActivity
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
            binding: ActivityAuthBinding,
            context: AuthActivity
        ) = with(binding) {
            authProgress.visibility = View.GONE
            errorTvAuth.text = message
            errorTvAuth.visibility = View.VISIBLE
            loginEdt.isEnabled = true
            passwordEdt.isEnabled = true
            loginBtn.visibility = View.VISIBLE
        }

    }

    object Success: AuthUiState{
        override fun apply(
            binding: ActivityAuthBinding,
            context: AuthActivity
        ) {

            context.startActivity(Intent(context, MainActivity::class.java))
            context.finish()
        }

    }
}