package com.example.musicapp.searchhistory.presentation

import androidx.lifecycle.LifecycleOwner
import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.favorites.testcore.TestDispatcherList
import com.example.musicapp.favorites.testcore.TestSearchQueryRepository
import com.example.musicapp.favorites.testcore.TestSingleUiStateCommunication
import com.example.musicapp.searchhistory.data.HistoryDeleteResult
import com.example.musicapp.searchhistory.data.SearchHistoryRepository
import com.example.musicapp.searchhistory.data.cache.HistoryItemCache
import com.example.musicapp.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 21.05.2023.
 **/
@OptIn(ExperimentalCoroutinesApi::class)
class SearchHistoryViewModelTest: ObjectCreator() {

    lateinit var viewModel: SearchHistoryViewModel
    lateinit var disptcherList: TestDispatcherList
    lateinit var singleUiStateCommunication: TestSingleUiStateCommunication
    lateinit var serchHistoryCommunication: TestSearchHistoryCommunication
    lateinit var repository: TestSearchHistoryRepository

    @Before
    fun setup(){
        repository = TestSearchHistoryRepository()
        disptcherList = TestDispatcherList()
        singleUiStateCommunication = TestSingleUiStateCommunication()
        serchHistoryCommunication = TestSearchHistoryCommunication()
        viewModel = SearchHistoryViewModel(
            historyRepository = repository,
            communication = serchHistoryCommunication,
            dispatchersList = disptcherList,
            mapper = HistoryDeleteResultMapper(singleUiStateCommunication)
        )

        repository.historyItems.addAll(
            listOf(
                HistoryItemCache("aaa",0),
                HistoryItemCache("ccc",1),
                HistoryItemCache("bbb",2),
            )
        )
    }

    @Test
    fun `test fetch history`(){
        viewModel.fetchHistory("")

        assertEquals(repository.historyItems.map { it.queryTerm }, serchHistoryCommunication.data)

        viewModel.fetchHistory(repository.historyItems.first().queryTerm)
        assertEquals(listOf(repository.historyItems.first().queryTerm), serchHistoryCommunication.data)
    }

    @Test
    fun `test save query`() {
        val query = "query"
        viewModel.saveQuery(query)
        assertEquals(query,repository.query)
    }

    @Test
    fun `test read query`() {
        val query = "query"
        repository.query = query
        assertEquals(query,viewModel.readQuery())
    }

    @Test
    fun `test clear history `() {
        viewModel.clearHistory()
        assertEquals(SingleUiEventState.ShowSnackBar.Success::class,singleUiStateCommunication.stateList.last()::class)

       repository.showError = true
        viewModel.clearHistory()
        assertEquals(SingleUiEventState.ShowSnackBar.Error::class,singleUiStateCommunication.stateList.last()::class)


    }

    @Test
    fun `test remove history item`() {
        viewModel.removeHistoryItem(repository.historyItems.first().queryTerm)
        assertEquals(SingleUiEventState.ShowSnackBar.Success::class,singleUiStateCommunication.stateList.last()::class)

        repository.showError = true
        viewModel.removeHistoryItem(repository.historyItems.first().queryTerm)
        assertEquals(SingleUiEventState.ShowSnackBar.Error::class,singleUiStateCommunication.stateList.last()::class)


    }


    class TestSearchHistoryCommunication: SearchHistoryCommunication{
        var data = mutableListOf<String>()

        override suspend fun collect(
            lifecycleOwner: LifecycleOwner,
            collector: FlowCollector<List<String>>,
        )  = Unit

        override fun map(newValue: List<String>) {
            data.clear()
            data.addAll(newValue)
        }

    }

    class TestSearchHistoryRepository: SearchHistoryRepository, TestSearchQueryRepository() {
        val historyItems = mutableListOf<HistoryItemCache>()
        var showError = false

        override fun getHistoryItems(query: String): Flow<List<String>> = flow {
           val result = historyItems.filter { it.queryTerm==query}.map { it.queryTerm }
            emit(if(result.isEmpty()) historyItems.map { it.queryTerm } else result)
        }

        override suspend fun clearHistory(): HistoryDeleteResult {
            val items  = mutableListOf<HistoryItemCache>()
            items.addAll(historyItems)
           historyItems.clear()
            return if(showError) HistoryDeleteResult.Error("")
            else HistoryDeleteResult.Success(items,"")
        }

        override suspend fun removeHistoryItem(item: String): HistoryDeleteResult {
            historyItems.removeIf { it.queryTerm==item }
            return if(showError) HistoryDeleteResult.Error("")
            else HistoryDeleteResult.Success(historyItems,"")
        }
    }
}