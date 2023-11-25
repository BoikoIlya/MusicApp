package com.kamancho.melisma.main.presentation

import androidx.lifecycle.LifecycleOwner
import com.kamancho.melisma.app.core.SDKChecker
import com.kamancho.melisma.app.core.SDKCheckerState
import com.kamancho.melisma.favorites.domain.FavoritesTracksInteractorTest
import com.kamancho.melisma.favorites.testcore.TestAuthRepository
import com.kamancho.melisma.favorites.testcore.TestTemporaryTracksCache
import com.kamancho.melisma.favorites.testcore.TestDispatcherList
import com.kamancho.melisma.favorites.testcore.TestFavoritesTracksInteractor
import com.kamancho.melisma.favorites.testcore.TestSingleUiStateCommunication
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.trending.data.ObjectCreator
import com.kamancho.melisma.updatesystem.data.UpdateResult
import com.kamancho.melisma.updatesystem.data.UpdateSystemRepository
import com.google.android.material.bottomsheet.BottomSheetBehavior
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.FlowCollector
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 17.03.2023.
 **/
class MainViewModelTest: ObjectCreator() {


   private lateinit var viewModel: MainViewModel
    private lateinit var playerCommunication: PlayerCommunication

    private lateinit var playerControlsCommunication: TestPlayerControlsCommunication
    private lateinit var currentQueueCommunication: TestCurrentQueueCommunication
    private lateinit var selectedTrackCommunication: TestSelectedTrackCommunication
    private lateinit var mediaController: TestMediaController
    private lateinit var bottomSheetCommunication: TestBottomSheetCommunication
    private lateinit var updateSystemRepo: TestUpdateSystemRepo
    private lateinit var firebaseMessagingWrapper: TestFirebaseMessagingWrapper
    private lateinit var temporaryTracksCache: TemporaryTracksCache
    private lateinit var singleUiStateCommunication: TestSingleUiStateCommunication
    private lateinit var authRepository: TestAuthRepository
    private lateinit var favoritesInteractor: TestFavoritesTracksInteractor
    private lateinit var actyvityNavigationCommuniacation: TestActyvityNavigationCommuniacation
    private lateinit var permissionCheckCommunication: TestPermissionCheckCommunication
    private lateinit var captchaRepository: FavoritesTracksInteractorTest.TestCaptchaRepository

    @Before
    fun setup(){
        captchaRepository = FavoritesTracksInteractorTest.TestCaptchaRepository()
        permissionCheckCommunication = TestPermissionCheckCommunication()
        favoritesInteractor = TestFavoritesTracksInteractor()
        actyvityNavigationCommuniacation = TestActyvityNavigationCommuniacation()
        authRepository = TestAuthRepository()
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
            dispatchersList = TestDispatcherList(),
            singleUiEventCommunication = singleUiStateCommunication,
            bottomSheetCommunication = bottomSheetCommunication,
            slideViewPagerCommunication = SlideViewPagerCommunication.Base(),
            firebaseMessagingWrapper =firebaseMessagingWrapper,
            favoritesInteractor =favoritesInteractor,
            authorizationRepository =authRepository,
            activityNavigationCommunication = actyvityNavigationCommuniacation,
            permissionCheckCommunication = permissionCheckCommunication,
            sdkChecker = TestSDKChecker(),
            controllerListener = TestControllerListener(),
            captchaRepository =captchaRepository,
        )
    }

    @Test
    fun `test init`(){
        assertEquals(1,firebaseMessagingWrapper.list.size)
        assertEquals(0,actyvityNavigationCommuniacation.states.size)
        assertEquals(PermissionCheckState.CheckForPermissions::class,permissionCheckCommunication.stateList.last()::class)
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
    fun `test dont show permission`(){
        viewModel.dontShowPermission()
        assertEquals(PermissionCheckState.Empty,permissionCheckCommunication.stateList.last())
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

        override suspend fun collectIgnoreLifecycle(collector: FlowCollector<Int>) = Unit

    }

    class TestUpdateSystemRepo: UpdateSystemRepository{
        var result: UpdateResult = UpdateResult.NewUpdateInfo("x","x","x")
        override suspend fun checkForNewVersion(): UpdateResult = result


    }



    class TestFirebaseMessagingWrapper: FirebaseInitializer{
        val list = emptyList<Int>().toMutableList()
        override fun init() {
            list.add(1)
        }

    }

    class TestActyvityNavigationCommuniacation: ActivityNavigationCommunication{
        val states = emptyList<ActivityNavigationState>().toMutableList()

        override fun map(newValue: ActivityNavigationState) {
            states.add(newValue)
        }

        override suspend fun collectIgnoreLifecycle(collector: FlowCollector<ActivityNavigationState>) = Unit

        override suspend fun collect(
            lifecycleOwner: LifecycleOwner,
            collector: FlowCollector<ActivityNavigationState>
        ) = Unit
    }

    class TestPermissionCheckCommunication: PermissionCheckCommunication{
        val stateList = emptyList<PermissionCheckState>().toMutableList()

        override suspend fun collect(
            lifecycleOwner: LifecycleOwner,
            collector: FlowCollector<PermissionCheckState>,
        ) = Unit

        override fun map(newValue: PermissionCheckState) {
            stateList.add(newValue)
        }

        override suspend fun collectIgnoreLifecycle(collector: FlowCollector<PermissionCheckState>) = Unit

    }

    class TestSDKChecker: SDKChecker{
        override fun check(state: SDKCheckerState, positive: () -> Unit, negative: () -> Unit) {
            return positive.invoke()
        }

    }

    class TestControllerListener: ControllerListener
}