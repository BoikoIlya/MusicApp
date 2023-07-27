package com.example.musicapp.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.DispatchersList
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
class FavoritesBottomSheetMenuViewModel @Inject constructor(
    private val resetSwipeActionCommunication: ResetSwipeActionCommunication,
    private val dispatchersList: DispatchersList
): ViewModel() {

    fun resetSwipedItem() = viewModelScope.launch(dispatchersList.io()) {
        resetSwipeActionCommunication.map(Unit)
    }
}