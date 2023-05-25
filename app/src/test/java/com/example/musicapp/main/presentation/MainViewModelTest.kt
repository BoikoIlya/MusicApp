package com.example.musicapp.main.presentation

import androidx.lifecycle.LifecycleOwner
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.favorites.testcore.TestTemporaryTracksCache
import com.example.musicapp.favorites.testcore.TestDispatcherList
import com.example.musicapp.favorites.testcore.TestFavoriteRepository
import com.example.musicapp.favorites.testcore.TestManagerResource
import com.example.musicapp.favorites.testcore.TestSingleUiStateCommunication
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.trending.data.ObjectCreator
import com.example.musicapp.updatesystem.data.MainViewModelMapper
import com.example.musicapp.updatesystem.data.UpdateResult
import com.example.musicapp.updatesystem.data.UpdateSystemRepository
import com.google.android.material.bottomsheet.BottomSheetBehavior
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 17.03.2023.
 **/
class MainViewModelTest: ObjectCreator() {


    lateinit var viewModel: MainViewModel
    lateinit var playerCommunication: PlayerCommunication

    lateinit var playerControlsCommunication: TestPlayerControlsCommunication
    lateinit var currentQueueCommunication: TestCurrentQueueCommunication
    lateinit var selectedTrackCommunication: TestSelectedTrackCommunication
    lateinit var mediaController: TestMediaController
    lateinit var bottomSheetCommunication: TestBottomSheetCommunication
    lateinit var updateSystemRepo: TestUpdateSystemRepo
    lateinit var firebaseMessagingWrapper: TestFirebaseMessagingWrapper
    lateinit var temporaryTracksCache: TemporaryTracksCache
    lateinit var singleUiStateCommunication: TestSingleUiStateCommunication

    @Before
    fun setup(){
        singleUiStateCommunication = TestSingleUiStateCommunication()
        temporaryTracksCache = TestTemporaryTracksCache()
        playerControlsCommunication = TestPlayerControlsCommunication()
        currentQueueCommunication = TestCurrentQueueCommunication()
        selectedTrackCommunication = TestSelectedTrackCommunication()
        mediaController = TestMediaController()
        bottomSheetCommunication = TestBottomSheetCommunication()
        updateSystemRepo = TestUpdateSystemRepo()
        firebaseMessagingWrapper = TestFirebaseMessagingWrapper()
        playerCommunication = PlayerCommunication.Base(
            playerControlsCommunication =playerControlsCommunication,
            currentQueueCommunication =currentQueueCommunication,
            selectedTrackCommunication =selectedTrackCommunication,
            singleUiEventCommunication = singleUiStateCommunication,
            trackDurationCommunication = TrackDurationCommunication.Base(),
            controller = mediaController
        )
        viewModel = MainViewModel(
            playerCommunication = playerCommunication,
            temporaryTracksCache = temporaryTracksCache,
            dispatchersList = TestDispatcherList(),
            singleUiEventCommunication = singleUiStateCommunication,
            bottomSheetCommunication = bottomSheetCommunication,
            slideViewPagerCommunication = SlideViewPagerCommunication.Base(),
            updateSystemRepository = updateSystemRepo,
            mapper = MainViewModelMapper.Base(
                DataTransfer.UpdateDialogTransfer.Base(),
                TestManagerResource(),singleUiStateCommunication),
            firebaseMessagingWrapper,
            TestFavoriteRepository()
        )
    }

    @Test
    fun `test init`(){
        assertEquals(1,firebaseMessagingWrapper.list.size)

        updateSystemRepo.result = UpdateResult.NoUpdate
        viewModel.checkForUpdate()
    }

    @Test
    fun `test bottom sheet communication`(){

        viewModel.bottomSheetState(BottomSheetBehavior.STATE_EXPANDED)
        assertEquals(1, bottomSheetCommunication.list.size)
        assertEquals(BottomSheetBehavior.STATE_EXPANDED, bottomSheetCommunication.list[0])
    }

    @Test
    fun `test player action`(){

        viewModel.playerAction(PlayerCommunicationState.Next)
        assertEquals(1, mediaController.nextTrackClicked)

        viewModel.playerAction(PlayerCommunicationState.Previous)
        assertEquals(1, mediaController.previousTrackClicked)


        viewModel.playerAction(PlayerCommunicationState.Pause)
        assertEquals(false, mediaController.isPlayingg)

        viewModel.playerAction(PlayerCommunicationState.Resume)
        assertEquals(true, mediaController.isPlayingg)
    }

    @Test
    fun `test save current page queue`()= runBlocking{
        viewModel.saveCurrentPageQueue(listOf(getMediaItem("2")))

        assertEquals("2", temporaryTracksCache.readCurrentPageTracks().first().mediaId)
    }

    @Test
    fun `test notification permission`(){
        viewModel.notificationPermissionCheck()
        assertEquals(SingleUiEventState.CheckForPermission::class,singleUiStateCommunication.stateList.last()::class)
    }

    @Test
    fun `test dont show permission`(){
        viewModel.dontShowPermission()
        assertEquals(1,singleUiStateCommunication.stateList.size)
    }

    class TestBottomSheetCommunication: BottomSheetCommunication{
        val list = emptyList<Int>().toMutableList()


        override suspend fun collect(
            lifecycleOwner: LifecycleOwner,
            collector: FlowCollector<Int>,
        ) = Unit

        override fun map(newValue: Int) {
           list.add(newValue)
        }

    }

    class TestUpdateSystemRepo: UpdateSystemRepository{
        var result: UpdateResult = UpdateResult.NewUpdateInfo("x","x","x")
        override suspend fun checkForNewVersion(): UpdateResult = result


    }



    class TestFirebaseMessagingWrapper: FirebaseMessagingWrapper{
        val list = emptyList<Int>().toMutableList()
        override fun subscribeToTopic() {
            list.add(1)
        }

    }
}