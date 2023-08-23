package com.kamancho.melisma.createplaylist.domain

import com.kamancho.melisma.addtoplaylist.domain.SelectedTrackDomain
import com.kamancho.melisma.addtoplaylist.presentation.SelectedTrackUi
import com.kamancho.melisma.app.core.CaptchaNeededException
import com.kamancho.melisma.app.core.HandleError
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.PlaylistDataInteractor
import com.kamancho.melisma.creteplaylist.data.PlaylistDataRepository
import com.kamancho.melisma.creteplaylist.presentation.SelectedTracksStore
import com.kamancho.melisma.favorites.domain.FavoritesTracksInteractorTest
import com.kamancho.melisma.favorites.testcore.TestAuthRepository
import com.kamancho.melisma.favorites.testcore.TestManagerResource
import com.kamancho.melisma.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 02.08.2023.
 **/
class PlaylistDataInteractorTest: ObjectCreator() {


    private lateinit var interactor: PlaylistDataInteractor
    private lateinit var captchaRepo: FavoritesTracksInteractorTest.TestCaptchaRepository
    private lateinit var managerResource: TestManagerResource
    private lateinit var playlistDataRepo: TestPlaylistDataRepo
    private lateinit var store: SelectedTracksStore

    @Before
    fun test(){
        store = SelectedTracksStore.Base()
        playlistDataRepo = TestPlaylistDataRepo()
        captchaRepo = FavoritesTracksInteractorTest.TestCaptchaRepository()
        managerResource = TestManagerResource()
        interactor = PlaylistDataInteractor.Base(
            handleResponse = HandleResponse.Base(TestAuthRepository(),captchaRepo,HandleError.Base(managerResource)),
            repository = playlistDataRepo,
            managerResource = managerResource,
            store = store,
            toTrackIdMapper = SelectedTrackDomain.ToIdMapper(),
            toTrackDomain = SelectedTrackUi.ToDomain()
        )

    }


    @Test
    fun `test create playlist`() = runBlocking {
        val expectedTitle = "title"
        val expectedDescription = "description"
        val expectedList = listOf(getSelectedTrackDomain(),getSelectedTrackDomain(1))
        store.saveList(expectedList)
        interactor.createPlaylist(expectedTitle,expectedDescription)

        assertEquals(expectedTitle,playlistDataRepo.title)
        assertEquals(expectedDescription,playlistDataRepo.description)
        assertEquals(expectedList.size,playlistDataRepo.audioIds.size)
    }

    @Test
    fun `test captcha`() = runBlocking {
        val expectedTitle = "title"
        val expectedDescription = "description"
        playlistDataRepo.throwCaptcha =true
        val expectedCaptchaId = "ddd"
        val expectedCaptchaUrl = "url"
        playlistDataRepo.captchaData = Pair(expectedCaptchaId,expectedCaptchaUrl)
        interactor.createPlaylist(expectedTitle,expectedDescription)

        assertEquals(expectedCaptchaId,captchaRepo.id)
        assertEquals(expectedCaptchaUrl,captchaRepo.url)
        assertEquals(true,captchaRepo.action!=null)
    }

    @Test
    fun `test edit playlist`() = runBlocking {
        val initialList = listOf(getSelectedTrackUi(),getSelectedTrackUi(1))
        val expectedTitle = "title"
        val expectedDescription = "description"
        val list = listOf(getSelectedTrackDomain(),getSelectedTrackDomain(2),getSelectedTrackDomain(3))
        store.saveList(list)
        interactor.editPlaylist(0,expectedTitle,expectedDescription,initialList)

        assertEquals(expectedTitle,playlistDataRepo.title)
        assertEquals(expectedDescription,playlistDataRepo.description)
        assertEquals(2,playlistDataRepo.listToAdd.size)
        assertEquals(1,playlistDataRepo.listToDelete.size)
    }


    @Test
    fun `test follow`() = runBlocking {
        val expectedPlaylistId = 2
        interactor.followPlaylist(expectedPlaylistId,0)
        assertEquals(expectedPlaylistId,playlistDataRepo.playlistId)
    }

    class TestPlaylistDataRepo: PlaylistDataRepository{
        var title = ""
        var description = ""
        val audioIds = emptyList<Int>().toMutableList()
        var playlistId = -1
        var throwCaptcha = false
        var captchaData = Pair("","")
        val listToAdd = emptyList<Int>().toMutableList()
        val listToDelete = emptyList<Int>().toMutableList()

        override suspend fun createPlaylist(
            title: String,
            description: String,
            audioIds: List<Int>,
        ) {
            this.title = title
            this.description = description
            this.audioIds.clear()
            this.audioIds.addAll(audioIds)
            if(throwCaptcha) throw CaptchaNeededException(captchaData.first,captchaData.second)
        }

        override suspend fun followPlaylist(playlistId: Int, playlistOwnerId: Int) {
           this.playlistId = playlistId
        }

        override suspend fun editPlaylist(
            playlistId: Int,
            title: String,
            description: String,
            tracksIdsToAdd: List<Int>,
            tracksIdsToDelete: List<Int>,
        ) {
            this.title = title
            this.description = description
            listToAdd.addAll(tracksIdsToAdd)
            listToDelete.addAll(tracksIdsToDelete)
        }

        override suspend fun removeFromPlaylist(playlistId: Int, audioIds: List<Int>) {
            this.audioIds.removeIf { it in audioIds }
        }

    }

}