package com.example.musicapp.vkauth.presentation

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentAuthBinding
import com.example.musicapp.favorites.presentation.FavoritesFragment

/**
 * Created by HP on 17.06.2023.
 **/
sealed interface AuthUiState{

    fun apply(
        binding: FragmentAuthBinding,
        manager: FragmentManager
    )

    object Empty: AuthUiState{
        override fun apply(binding: FragmentAuthBinding, manager: FragmentManager) = Unit
    }

    object Loading: AuthUiState{

        override fun apply(binding: FragmentAuthBinding,manager: FragmentManager)= with(binding) {
            authProgress.visibility = View.VISIBLE
            loginEdt.isEnabled = false
            passwordEdt.isEnabled = false
            loginBtn.visibility = View.GONE
        }

    }

    object Error: AuthUiState{
        override fun apply(binding: FragmentAuthBinding,manager: FragmentManager) = with(binding) {
            authProgress.visibility = View.GONE
            loginEdt.isEnabled = true
            passwordEdt.isEnabled = true
            loginBtn.visibility = View.VISIBLE
        }

    }
}