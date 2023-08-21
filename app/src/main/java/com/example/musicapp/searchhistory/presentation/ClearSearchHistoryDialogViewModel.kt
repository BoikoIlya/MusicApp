package com.example.musicapp.searchhistory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.searchhistory.data.HistoryDeleteResult
import com.example.musicapp.searchhistory.data.SearchHistoryRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 30.06.2023.
 **/
class ClearSearchHistoryDialogViewModel @Inject constructor(
    private val dispatchersList: DispatchersList,
    private val repository: SearchHistoryRepository,
    private val mapper:  HistoryDeleteResult.Mapper<Unit>
): ViewModel() {

    fun clearHistory() = viewModelScope.launch(dispatchersList.io()) {
        repository.clearHistory(repository.readQueryAndHistoryType().second).map(mapper)
    }

}