package com.example.musicapp.searchhistory.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.R
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.searchhistory.data.HistoryDeleteResult
import com.example.musicapp.searchhistory.data.SearchHistoryRepository
import com.example.musicapp.searchhistory.data.cache.HistoryItemCache
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 13.08.2023.
 **/
abstract class SearchHistoryListViewModel(
    private val dispatchersList: DispatchersList,
    private val historyRepository: SearchHistoryRepository,
    private val communication: SearchHistoryCommunication,
    private val mapper: HistoryDeleteResult.Mapper<Unit>,
    private val searchQueryCommunication: SearchQueryCommunication,
    private val searchHistorySingleStateCommunication: SearchHistorySingleStateCommunication,
    private val historyType: Int
): ViewModel() {

    private var historyListJob: Job? = null


    init {
        fetchHistory("")
    }

    fun fetchHistory(query: String ="") {

        historyListJob?.cancel()
        historyListJob = viewModelScope.launch(dispatchersList.io()) {
            historyRepository.getHistoryItems(query,historyType).collectLatest {
                communication.map(it)
            }
        }
    }

    fun removeHistoryItem(item: String) = viewModelScope.launch(dispatchersList.io()) {
        val result = historyRepository.removeHistoryItem(item,historyType)
        result.map(mapper)
    }

    fun singleState(state: SearchHistorySingleState) = viewModelScope.launch(dispatchersList.io()) {
        searchHistorySingleStateCommunication.map(state)
    }


    suspend fun collectSearchHistory(
        owner: LifecycleOwner,
        collector: FlowCollector<List<String>>
    ) = communication.collect(owner, collector)

    suspend fun collectQueryCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<String>
    ) = searchQueryCommunication.collect(owner, collector)

}

class BaseSearchHistoryTracksListViewModel @Inject constructor(
    dispatchersList: DispatchersList,
    historyRepository: SearchHistoryRepository,
    communication: SearchHistoryCommunication,
    mapper: HistoryDeleteResult.Mapper<Unit>,
    searchQueryCommunication: SearchQueryCommunication,
    searchHistorySingleStateCommunication: SearchHistorySingleStateCommunication
): SearchHistoryListViewModel(
    dispatchersList,
    historyRepository,
    communication,
    mapper,
    searchQueryCommunication,
    searchHistorySingleStateCommunication,
    HistoryItemCache.TYPE_TRACK
)

class BaseSearchHistoryPlaylistsListViewModel @Inject constructor(
    dispatchersList: DispatchersList,
    historyRepository: SearchHistoryRepository,
    communication: SearchHistoryCommunication,
    mapper: HistoryDeleteResult.Mapper<Unit>,
    searchQueryCommunication: SearchQueryCommunication,
    searchHistorySingleStateCommunication: SearchHistorySingleStateCommunication
): SearchHistoryListViewModel(
    dispatchersList,
    historyRepository,
    communication,
    mapper,
    searchQueryCommunication,
    searchHistorySingleStateCommunication,
    HistoryItemCache.TYPE_PLAYLIST
)