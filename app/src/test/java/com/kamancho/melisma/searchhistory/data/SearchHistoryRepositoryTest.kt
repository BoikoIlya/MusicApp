package com.kamancho.melisma.searchhistory.data

import com.kamancho.melisma.favorites.testcore.TestManagerResource
import com.kamancho.melisma.searchhistory.data.cache.HistoryDao
import com.kamancho.melisma.searchhistory.data.cache.HistoryItemCache
import com.kamancho.melisma.searchhistory.data.cache.SearchQueryTransfer
import com.kamancho.melisma.searchhistory.presentation.ToHistoryItemMapper
import com.kamancho.melisma.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 20.05.2023.
 **/
class SearchHistoryRepositoryTest: ObjectCreator() {

    lateinit var repository: SearchHistoryRepository
    lateinit var managerResource: TestManagerResource
    lateinit var searchQueryTransfer: SearchQueryTransfer
    lateinit var cache: TestHistoryDao

    @Before
    fun setup(){
        searchQueryTransfer = SearchQueryTransfer.Base()
        managerResource = TestManagerResource()
        cache = TestHistoryDao()
        repository = SearchHistoryRepository.Base(
            cache = cache,
            mapper =ToHistoryItemMapper.Base(),
            managerResource =managerResource,
            transfer = searchQueryTransfer
        )

        cache.list.addAll(
            listOf(
                getHisstoryItem("a",0),
                getHisstoryItem("c",1),
                getHisstoryItem("b",2),
            )
        )
    }


    @Test
    fun `test get history items`() = runBlocking {
       val result1 = repository.getHistoryItems("").first()
        assertEquals(cache.list.sortedByDescending { it.time }.map { it.queryTerm }, result1)

        val result2 = repository.getHistoryItems(cache.list.first().queryTerm).first()
        assertEquals(listOf(cache.list.first().queryTerm), result2)

        val nonexistentQuery = "dknsfsklfdgms"
        val result3 = repository.getHistoryItems(nonexistentQuery).first()
        assertEquals(listOf(nonexistentQuery), result3)
    }

    @Test
    fun `test clear history`() = runBlocking {
        val message = "message"
        managerResource.valueString = message
        val result1 = repository.clearHistory()
        assertEquals(emptyList<HistoryItemCache>(),cache.list)
        assertEquals(HistoryDeleteResult.Success(emptyList(),message)::class,result1::class)

        val result2 = repository.clearHistory()
        assertEquals(HistoryDeleteResult.Error(message),result2)

    }

    @Test
    fun `test remove item`() = runBlocking{

        val message = "message"
        managerResource.valueString = message
        val result1 = repository.removeHistoryItem(cache.list.first().queryTerm)
        assertEquals(2,cache.list.size)
        assertEquals(HistoryDeleteResult.Success(emptyList(),message)::class,result1::class)

        repository.removeHistoryItem(cache.list.first().queryTerm)
        repository.removeHistoryItem(cache.list.first().queryTerm)
        val result2 = repository.removeHistoryItem("aaa")
        assertEquals(HistoryDeleteResult.Error(message),result2)
    }

    @Test
    fun `test save history`() = runBlocking{
        val query = "blabla"
        repository.saveQueryInDB(query)

        assertEquals(4,cache.list.size)
        assertEquals(query,searchQueryTransfer.read())

        repository.saveQueryInDB("")
        assertEquals(4,cache.list.size)
    }

    class TestHistoryDao: HistoryDao{
        val list = mutableListOf<HistoryItemCache>()

        override suspend fun insertHistoryItem(item: HistoryItemCache) {
            list.add(item)
        }

        override fun searchHistory(query: String): Flow<List<HistoryItemCache>> {
            return flow {
             val result = list.find { it.queryTerm==query }
            emit(if(result==null) emptyList() else listOf(result))
            }

        }

        override fun getAllHistoryByTime(): Flow<List<HistoryItemCache>> {
            return flow {  emit( list.sortedByDescending { it.time })}
        }

        override suspend fun clearHistory() {
            list.clear()
        }

        override suspend fun removeItem(id: String) {
            list.removeIf { it.queryTerm==id }
        }

    }
}