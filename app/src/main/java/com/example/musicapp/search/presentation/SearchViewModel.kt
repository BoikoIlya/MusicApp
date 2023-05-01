package com.example.musicapp.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.search.data.SearchRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 01.05.2023.
 **/
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
): ViewModel() {

    init {

    }

    suspend fun frtchdat() = repository.searchTracksByName("d")
}