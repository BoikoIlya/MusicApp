package com.example.musicapp.frienddetails.presentation

import androidx.lifecycle.LifecycleOwner
import com.example.musicapp.app.core.FavoritesViewModel
import com.example.musicapp.favorites.presentation.FavoritesUiState
import kotlinx.coroutines.flow.FlowCollector

/**
 * Created by HP on 18.08.2023.
 **/
interface FriendsDataViewModel {

    fun search(query: String)

    suspend fun collectSearchQuery(
        owner: LifecycleOwner,
        collector: FlowCollector<String>)
}