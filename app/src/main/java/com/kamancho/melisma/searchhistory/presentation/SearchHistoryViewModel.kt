package com.kamancho.melisma.searchhistory.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.searchhistory.data.SearchHistoryRepository
import com.kamancho.melisma.searchhistory.data.cache.HistoryItemCache
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 02.05.2023.
 **/
class SearchHistoryViewModel @Inject constructor(
    private val historyRepository: SearchHistoryRepository,
    private val dispatchersList: DispatchersList,
    private val searchHistoryInputStateCommunication: SearchHistoryInputStateCommunication,
    private val searchHistorySingleStateCommunication:SearchHistorySingleStateCommunication,
    private val managerResource: ManagerResource,
    private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val searchQueryCommunication: SearchQueryCommunication,
): ViewModel() {


    private var currentHistoryType = HistoryItemCache.TYPE_TRACK
    private var currentQuery: String? =null

    companion object{
        private const val emptyPageIndex = -1
    }

    fun findInHistory(query: String) {
        if (query.isNotEmpty()) searchHistoryInputStateCommunication.map(SearchHistoryTextInputState.ResetError)
        if(currentQuery==query && currentQuery!=null) return
        currentQuery = query
        searchQueryCommunication.map(query)
    }

    fun changeHistoryType(type: Int) {
        currentHistoryType = type
    }

    fun checkQueryBeforeNavigation(query: String) =viewModelScope.launch(dispatchersList.io()) {
        val trimmedQuery = query.trim()
        if(trimmedQuery.isNotEmpty()){
            searchHistoryInputStateCommunication.map(SearchHistoryTextInputState.ResetError)
            historyRepository.saveQueryInDB(trimmedQuery,currentHistoryType)
            searchHistorySingleStateCommunication.map(SearchHistorySingleState.NavigateToSearch(query,currentHistoryType))
        }else {
            searchHistoryInputStateCommunication.map(SearchHistoryTextInputState.ShowError(
                managerResource.getString(R.string.empty_input)
            ))
        }
    }

    fun launchClearHistoryDialog() = viewModelScope.launch(dispatchersList.io()) {
        if(historyRepository.getHistoryItems("",currentHistoryType).first().isEmpty()) {
            globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(
                managerResource.getString(R.string.the_history_is_empty)
            ))
        }else {
            historyRepository.saveQueryInDB("",currentHistoryType)
            globalSingleUiEventCommunication.map(SingleUiEventState.ShowDialog(ClearSearchHistoryDialogFragment()))
        }
    }

    fun readPageIndexState(): PageIndexState {
        val index = historyRepository.readQueryAndHistoryType().second
        val state = if(index== emptyPageIndex) PageIndexState.Empty
        else PageIndexState.SetIndex(index)
        return state.apply {
           viewModelScope.launch(dispatchersList.io()) {
               historyRepository.saveQueryInDB("",emptyPageIndex)
           }
       }
    }

    suspend fun collectSearchHistoryInputStateCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<SearchHistoryTextInputState>
    ) = searchHistoryInputStateCommunication.collect(owner, collector)

    suspend fun collectSearchHistorySingleStateCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<SearchHistorySingleState>
    ) = searchHistorySingleStateCommunication.collect(owner, collector)


}