package com.example.musicapp.searchhistory.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.searchhistory.data.HistoryDeleteResult
import com.example.musicapp.searchhistory.data.SearchHistoryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 02.05.2023.
 **/
class SearchHistoryViewModel @Inject constructor(
    private val historyRepository: SearchHistoryRepository,
    private val communication: SearchHistoryCommunication,
    private val dispatchersList: DispatchersList,
    private val mapper: HistoryDeleteResult.Mapper<Unit>
): ViewModel() {

    init {
        fetchHistory()
    }

    private var historyListJob: Job? = null

    fun fetchHistory(query: String ="") {
        historyListJob?.cancel()

        historyListJob = viewModelScope.launch(dispatchersList.io()) {
            historyRepository.getHistoryItems(query).collectLatest {
                communication.map(it)
            }
        }
    }

    fun saveQuery(query: String) = viewModelScope.launch(dispatchersList.io()){
        historyRepository.saveQuery(query)
    }

    fun readQuery() = historyRepository.readQuery()

    fun removeHistoryItem(item: String) = viewModelScope.launch(dispatchersList.io()) {
       val result = historyRepository.removeHistoryItem(item)
        result.map(mapper)
    }

    fun clearHistory() = viewModelScope.launch(dispatchersList.io()) {
        val result  = historyRepository.clearHistory()
        result.map(mapper)
    }


    suspend fun collectSearchHistory(
        owner: LifecycleOwner,
        collector: FlowCollector<List<String>>
    ) = communication.collect(owner, collector)
}