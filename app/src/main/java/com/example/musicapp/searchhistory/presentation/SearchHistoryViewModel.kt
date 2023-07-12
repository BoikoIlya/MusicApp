package com.example.musicapp.searchhistory.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.R
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.PlayerControlsCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.main.presentation.CollectPlayerControls
import com.example.musicapp.main.presentation.PlayerControlsState
import com.example.musicapp.searchhistory.data.HistoryDeleteResult
import com.example.musicapp.searchhistory.data.SearchHistoryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 02.05.2023.
 **/
class SearchHistoryViewModel @Inject constructor(
    private val historyRepository: SearchHistoryRepository,
    private val communication: SearchHistoryCommunication,
    private val dispatchersList: DispatchersList,
    private val mapper: HistoryDeleteResult.Mapper<Unit>,
    private val searchQueryCommunication: SearchQueryCommunication,
    private val searchHistoryInputStateCommunication: SearchHistoryInputStateCommunication,
    private val searchHistorySingleStateCommunication:SearchHistorySingleStateCommunication,
    private val managerResource: ManagerResource,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val playerControls: PlayerControlsCommunication
): ViewModel(), CollectPlayerControls {

    init {
        fetchHistory()
    }

    private var historyListJob: Job? = null

    fun fetchHistory(query: String ="") {
        if (query.isNotEmpty()) searchHistoryInputStateCommunication.map(SearchHistoryTextInputState.ResetError)

         historyListJob?.cancel()

        historyListJob = viewModelScope.launch(dispatchersList.io()) {
            historyRepository.getHistoryItems(query).collectLatest {
                communication.map(it)
            }
        }
    }

    fun checkQueryBeforeNavigation(query: String) =viewModelScope.launch(dispatchersList.io()) {
        val trimmedQuery = query.trim()
        if(trimmedQuery.isNotEmpty()){
            searchHistoryInputStateCommunication.map(SearchHistoryTextInputState.ResetError)
            historyRepository.saveQueryInDB(trimmedQuery)
            searchQueryCommunication.map(trimmedQuery)
            searchHistorySingleStateCommunication.map(SearchHistorySingleState.NavigateToSearchFragment)
        }else {
            searchHistoryInputStateCommunication.map(SearchHistoryTextInputState.ShowError(
                managerResource.getString(R.string.empty_input)
            ))
        }
    }

    fun launchClearHistoryDialog() = viewModelScope.launch(dispatchersList.io()) {
        if(historyRepository.getHistoryItems("").first().isEmpty()) {
            globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(
                managerResource.getString(R.string.the_history_is_empty)
            ))
        }else globalSingleUiEventCommunication.map(SingleUiEventState.ShowDialog(ClearSearchHistoryDialogFragment()))
    }

    fun saveQueryToCommuniction(query: String) = searchQueryCommunication.map(query)

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

    suspend fun collectSearchQuery(
        owner: LifecycleOwner,
        collector: FlowCollector<String>
    ) = searchQueryCommunication.collect(owner, collector)

    suspend fun collectSearchHistoryInputStateCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<SearchHistoryTextInputState>
    ) = searchHistoryInputStateCommunication.collect(owner, collector)

    suspend fun collectSearchHistorySingleStateCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<SearchHistorySingleState>
    ) = searchHistorySingleStateCommunication.collect(owner, collector)

    override suspend fun collectPlayerControls(
        owner: LifecycleOwner,
        collector: FlowCollector<PlayerControlsState>,
    ) = playerControls.collect(owner, collector)

}