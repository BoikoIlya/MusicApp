package com.example.musicapp.main.presentation

import android.Manifest
import android.os.Build
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.SDKChecker
import com.example.musicapp.app.core.SDKCheckerState
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.TracksResultEmptyMapper
import com.example.musicapp.favorites.domain.FavoritesTracksInteractor
import com.example.musicapp.favorites.presentation.UiCommunication
import com.example.musicapp.main.data.CheckAuthRepository
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.vkauth.presentation.AuthActivity
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 31.01.2023.
 **/
class MainViewModel @Inject constructor(
    private val playerCommunication: PlayerCommunication,
    private val temporaryTracksCache: TemporaryTracksCache,
    private val dispatchersList: DispatchersList,
    private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val bottomSheetCommunication: BottomSheetCommunication,
    private val firebaseMessagingWrapper: FirebaseMessagingWrapper,
    favoritesInteractor: FavoritesTracksInteractor,
    private val authorizationRepository: CheckAuthRepository,
    private val activityNavigationCommunication: ActivityNavigationCommunication,
    private val slideViewPagerCommunication: SlideViewPagerCommunication,
    private val permissionCheckCommunication: PermissionCheckCommunication,
    private val sdkChecker: SDKChecker
): BaseViewModel<Unit>(
    playerCommunication,
    UiCommunication.EmptyCommunication(),
    temporaryTracksCache,
    dispatchersList,
    favoritesInteractor,
    TracksResultEmptyMapper(),
    TrackChecker.Empty
    ),
    CollectPlayerControls{

    private var showPermission = true

    companion object{
        const val permissionRequestCode = 0
    }

    init {
        checkAuth()
        firebaseMessagingWrapper.subscribeToTopic()
        notificationPermissionCheck()
    }

    fun bottomSheetState(state: Int){
        bottomSheetCommunication.map(state)
    }

    fun notificationPermissionCheck()  {
        sdkChecker.check(SDKCheckerState.AboveApi32,{
            permissionCheckCommunication.map(
                PermissionCheckState.CheckForPermission(
                    Manifest.permission.POST_NOTIFICATIONS,
                    permissionRequestCode))
        },{})

    }

    fun checkAuth() = viewModelScope.launch(dispatchersList.io()) {
        authorizationRepository.isNotAuthorized().collect{
            if(it) activityNavigationCommunication.map(ActivityNavigationState.Navigate(AuthActivity::class.java))
        }
    }

    fun dontShowPermission() { permissionCheckCommunication.map(PermissionCheckState.Empty)}

    override suspend fun collectPlayerControls(
        owner: LifecycleOwner,
        collector: FlowCollector<PlayerControlsState>,
    ) = playerCommunication.collectPlayerControls(owner,collector)


    suspend fun collectSingleUiUpdateCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<SingleUiEventState>
    ) = singleUiEventCommunication.collect(owner,collector)

    suspend fun collectBottomSheetState(
        owner: LifecycleOwner,
        collector: FlowCollector<Int>
    ) = bottomSheetCommunication.collect(owner, collector)


    suspend fun collectActivityNavigationCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<ActivityNavigationState>
    ) = activityNavigationCommunication.collect(owner,collector)

    suspend fun collectSlideViewPagerCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<Int>
    ) = slideViewPagerCommunication.collect(owner,collector)

    suspend fun collectPermissionCheckCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<PermissionCheckState>
    ) = permissionCheckCommunication.collect(owner,collector)
}