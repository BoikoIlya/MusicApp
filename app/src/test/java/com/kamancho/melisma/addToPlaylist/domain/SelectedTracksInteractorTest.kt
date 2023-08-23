package com.kamancho.melisma.addToPlaylist.domain

import android.view.View
import com.kamancho.melisma.addtoplaylist.domain.SelectedTrackDomain
import com.kamancho.melisma.addtoplaylist.domain.SelectedTracksInteractor
import com.kamancho.melisma.addtoplaylist.presentation.SelectedTrackUi
import com.kamancho.melisma.app.core.CacheRepository
import com.kamancho.melisma.creteplaylist.presentation.SelectedTracksStore
import com.kamancho.melisma.favorites.data.SortingState
import com.kamancho.melisma.favorites.testcore.TestManagerResource
import com.kamancho.melisma.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 01.08.2023.
 **/
class SelectedTracksInteractorTest: ObjectCreator() {

    private lateinit var selectedTracksInteractor: SelectedTracksInteractor
    private lateinit var repository: TestCachedTracksRepositorySelectedTrackDomain
    private lateinit var managerResource: TestManagerResource
    private val store =  SelectedTracksStore.Base()

    @Before
    fun setup(){
        managerResource = TestManagerResource()
        repository = TestCachedTracksRepositorySelectedTrackDomain()
        selectedTracksInteractor = SelectedTracksInteractor.Base(
            toDomainMapper = SelectedTrackUi.ToDomain(),
            repository = repository,
            store = store,
            managerResource = managerResource
        )
    }

    @Test
    fun `test map`() = runBlocking {
        repository.list.addAll(listOf(getSelectedTrackDomain(),getSelectedTrackDomain(1)))
       val actual1 = selectedTracksInteractor.map(SortingState.ByTime(""),0).first()
        assertEquals(2,actual1.size)
        assertEquals(false,actual1[0].map(CheckSelectability()))
        assertEquals(false,actual1[1].map(CheckSelectability()))

        store.saveList(listOf(getSelectedTrackDomain()))
        val actual2 = selectedTracksInteractor.map(SortingState.ByTime(""),0).first()
        assertEquals(2,actual2.size)
        assertEquals(true,actual2[0].map(CheckSelectability()))
        assertEquals(false,actual2[1].map(CheckSelectability()))
    }

    @Test
    fun `test selected tracks`() = runBlocking {
        repository.list.addAll(listOf(getSelectedTrackDomain(),getSelectedTrackDomain(1)))
        val actual1 = selectedTracksInteractor.selectedTracks()
        assertEquals(0,actual1.size)


        store.saveList(listOf(getSelectedTrackDomain()))
        val actual2 = selectedTracksInteractor.map(SortingState.ByTime(""),0).first()
        assertEquals(2,actual2.size)
    }

    @Test
    fun `test handle track`() = runBlocking {
        selectedTracksInteractor.handleItem(getSelectedTrackUi())
        assertEquals(1,store.read().size)

        selectedTracksInteractor.handleItem(getSelectedTrackUi())
        assertEquals(0,store.read().size)
    }

    class TestCachedTracksRepositorySelectedTrackDomain: CacheRepository<SelectedTrackDomain> {
        var isdbEmpty = false
        val list = emptyList<SelectedTrackDomain>().toMutableList()

        override fun fetch(
            sortingState: SortingState,
            playlistId: Int,
        ): Flow<List<SelectedTrackDomain>> = flow {  emit(list)}

        override suspend fun isDbEmpty(playlistId: Int): Boolean = isdbEmpty
    }

    class CheckSelectability: SelectedTrackUi.Mapper<Boolean>{
        override fun map(
            id: Int,
            title: String,
            author: String,
            durationFormatted: String,
            smallImageUrl: String,
            selectedIconVisibility: Int,
            backgroundColor: Int,
        ): Boolean {
            return selectedIconVisibility == View.VISIBLE
        }


    }
}