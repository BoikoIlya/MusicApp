package com.example.musicapp.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.SearchQueryRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 14.08.2023.
 **/
class SearchViewModel @Inject constructor(
    private val queryRepository: SearchQueryRepository,
    private val dispatchersList: DispatchersList
): ViewModel() {

    fun readQuery(): Pair<String,Int> = queryRepository.readQueryAndHistoryType()

    fun savePageIndex(index: Int) = viewModelScope.launch(dispatchersList.io()){
        queryRepository.saveQueryInDB(queryRepository.readQueryAndHistoryType().first,index)
    }


}