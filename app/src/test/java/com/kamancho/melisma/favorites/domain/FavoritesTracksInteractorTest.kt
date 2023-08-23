package com.kamancho.melisma.favorites.domain

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.DuplicateException
import com.kamancho.melisma.app.core.HandleError
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.app.core.MediaItemToTrackDomainMapper
import com.kamancho.melisma.app.core.UnAuthorizedException
import com.kamancho.melisma.captcha.data.CaptchaRepository
import com.kamancho.melisma.captcha.data.RepeatActionAfterCaptcha
import com.kamancho.melisma.favorites.data.FavoritesTracksRepository
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.favorites.testcore.TestAuthRepository
import com.kamancho.melisma.favorites.testcore.TestManagerResource
import com.kamancho.melisma.trending.data.ObjectCreator
import com.kamancho.melisma.trending.domain.TrackDomain
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException
import kotlin.random.Random

/**
 * Created by HP on 10.07.2023.
 **/
class FavoritesTracksInteractorTest: ObjectCreator() {

    private lateinit var interactor: FavoritesTracksInteractor
    private lateinit var repository: TestFavoritesTracksRepository
    private lateinit var authRepository: TestAuthRepository
    private lateinit var managerResource: ManagerResource
    private lateinit var transfer: DataTransfer<TrackDomain>
    private lateinit var captchaRepository: TestCaptchaRepository

    @Before
    fun setup(){
        captchaRepository = TestCaptchaRepository()
        repository = TestFavoritesTracksRepository()
        authRepository = TestAuthRepository()
        managerResource = TestManagerResource()
        transfer = DataTransfer.MusicDialogTransfer()
        interactor = FavoritesTracksInteractor.Base(
            repository =repository,
            handleResponse = HandleResponse.Base(authRepository,captchaRepository,HandleError.Base(managerResource)),
            managerResource = managerResource,
            uiToDomainMapper = MediaItemToTrackDomainMapper.Base(),
            transfer = transfer
        )
    }



    @Test
    fun `add to favorites if not duplicated`()= runBlocking {


        repository.duplicateException = true

        val result2 = interactor.addToFavoritesIfNotDuplicated(MediaItem.EMPTY)
        assertEquals(TracksResult.Failure::class,result2::class)

    }

    @Test
    fun `test contains`() = runBlocking {
        repository.list.add(getTrackDomain(name = "2", author = "2"))
       val actual = interactor.containsTrackInDb(Pair("2","2"))
        assertEquals(true,actual)
    }


    @Test
    fun `test delete`() = runBlocking {
        repository.list.add(getTrackDomain(id = 2,name = "2", author = "2"))
        val actual1 = interactor.deleteData(2)
        assertEquals(TracksResult.Success::class,actual1::class)


    }


    class TestFavoritesTracksRepository: FavoritesTracksRepository {
        val list = emptyList<TrackDomain>().toMutableList()
        var duplicateException = false
        var userId = 0
        var shouldFaileUpdate = false
        var unAuthorizedException = false

        override suspend fun containsInDb(data: Pair<String, String>): Boolean {
            return list.find { it.map(ContainsMapper(data)) } !=null
        }

        override suspend fun addToFavoritesIfNotDuplicated(data: TrackDomain): Int {
            if(duplicateException) throw DuplicateException()
            else{
                list.add(data)
                return Random.nextInt()
            }
        }

        override suspend fun addToFavorites(data: TrackDomain): Int {
            list.add(data)
            return Random.nextInt()
        }

        override suspend fun userId(): Int = userId

        override suspend fun deleteData(id: Int) {
          list.removeIf { it.map(ContainsByIdMapper(id)) }
        }

        override suspend fun updateData() {
            if (shouldFaileUpdate) throw UnknownHostException()
            else if(unAuthorizedException) throw UnAuthorizedException()
        }

    }

    class ContainsMapper(
        private val data: Pair<String,String>
    ): TrackDomain.Mapper<Boolean>{
        override fun map(
            id: Int,
            track_url: String,
            smallImgUrl: String,
            bigImgUrl: String,
            name: String,
            artistName: String,
            albumName: String,
            date: Int,
            duration: Int,
            ownerId: Int,
        ): Boolean {
            return name == data.first && artistName == data.second
        }

    }

    class ContainsByIdMapper(
        private val id: Int
    ): TrackDomain.Mapper<Boolean>{
        override fun map(
            id: Int,
            track_url: String,
            smallImgUrl: String,
            bigImgUrl: String,
            name: String,
            artistName: String,
            albumName: String,
            date: Int,
            duration: Int,
            ownerId: Int,
        ): Boolean {
            return id == this.id
        }

    }


    class TestCaptchaRepository: CaptchaRepository {
         var id = ""
         var url = ""
         var action: RepeatActionAfterCaptcha?=null
         var enteredData = ""

        override fun saveNewCaptchaData(
            id: String,
            url: String,
            actionCausedCaptcha: RepeatActionAfterCaptcha,
        ) {
            this.id = id
            this.url = url
            this.action = actionCausedCaptcha
        }

        override fun saveEnteredDataFromCaptcha(data: String) {
            enteredData = data
        }

        override fun clearCaptcha() {
            id = ""
            url = ""
            action = null
            enteredData = ""
        }

        override suspend fun collectCaptchaData(collector: FlowCollector<Pair<String, RepeatActionAfterCaptcha>>) = Unit
    }

}