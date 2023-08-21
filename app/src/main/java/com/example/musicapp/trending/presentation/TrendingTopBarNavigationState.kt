package com.example.musicapp.trending.presentation

import androidx.navigation.NavController
import com.example.musicapp.app.core.ClickListener

/**
 * Created by HP on 07.08.2023.
 **/
sealed interface TrendingTopBarNavigationState{

    fun apply(navController: NavController)

    data class Navigate(
        private val destination: Int
    ): TrendingTopBarNavigationState {

        override fun apply(navController: NavController) {
            navController.navigate(destination)
        }
    }

    object Empty: TrendingTopBarNavigationState {
        override fun apply(navController: NavController) = Unit
    }
}