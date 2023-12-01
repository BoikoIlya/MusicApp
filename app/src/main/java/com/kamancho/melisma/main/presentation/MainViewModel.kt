package com.kamancho.melisma.main.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.app.core.BaseViewModel
import com.kamancho.melisma.captcha.data.CaptchaRepository
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SDKChecker
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultEmptyMapper
import com.kamancho.melisma.captcha.presentation.CaptchaFragmentDialog
import com.kamancho.melisma.downloader.presentation.DownloadCompleteCommunication
import com.kamancho.melisma.downloader.presentation.DownloadResult
import com.kamancho.melisma.favorites.domain.FavoritesTracksInteractor
import com.kamancho.melisma.favorites.presentation.UiCommunication
import com.kamancho.melisma.main.data.CheckAuthRepository
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.notifications.presentation.NotificationsUpdateOnAppStart
import com.kamancho.melisma.vkauth.presentation.AuthActivity
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 31.01.2023.
 **/

class MainViewModel @Inject constructor(
    private val playerCommunication: PlayerCommunication,
    private val dispatchersList: DispatchersList,
    private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val bottomSheetCommunication: BottomSheetCommunication,
    private val firebaseInitializer: FirebaseInitializer,
    favoritesInteractor: FavoritesTracksInteractor,
    private val authorizationRepository: CheckAuthRepository,
    private val activityNavigationCommunication: ActivityNavigationCommunication,
    private val slideViewPagerCommunication: SlideViewPagerCommunication,
    private val permissionCheckCommunication: PermissionCheckCommunication,
    private val sdkChecker: SDKChecker,
    private val captchaRepository: CaptchaRepository,
    private val notificationsUpdateOnAppStart: NotificationsUpdateOnAppStart,
    private val downloadCompleteCommunication: DownloadCompleteCommunication
): BaseViewModel<Unit>(
    playerCommunication,
    UiCommunication.EmptyCommunication(),
    TemporaryTracksCache.Empty,
    dispatchersList,
    favoritesInteractor,
    TracksResultEmptyMapper(),
    TrackChecker.Empty
    ),
    CollectPlayerControls{


    companion object{
        const val permissionRequestCode = 0
    }

    init {
        checkAuth()
        firebaseInitializer.init()
        updateNotifications()
        permissions()
        handleCaptcha()
    }



    fun bottomSheetState(state: Int){
        bottomSheetCommunication.map(state)
    }

    fun permissions(){
        val permissions = mutableListOf<String>()
        val chain: PermissionsChain =
            PermissionsChain.NotificationPermission(
                PermissionsChain.WriteExternalStoragePermission(
                    PermissionsChain.LastChainItem()))

        permissionCheckCommunication.map(
            PermissionCheckState.CheckForPermissions(
                chain.check(sdkChecker, permissions), permissionRequestCode
            )
        )
    }


    fun checkAuth() = viewModelScope.launch(dispatchersList.io()) {
        authorizationRepository.isNotAuthorized().collect{
            activityNavigationCommunication.map(
            if(it) ActivityNavigationState.Navigate(AuthActivity::class.java)
            else ActivityNavigationState.LoggedIn
            )
        }
    }

    fun dontShowPermission() {
        permissionCheckCommunication.map(PermissionCheckState.Empty)

    }

    fun updateNotifications(){
        notificationsUpdateOnAppStart.update(viewModelScope)
    }

    fun clearDisablePlayer() = playerCommunication.map(PlayerCommunicationState.Disabled)


    fun handleCaptcha() = viewModelScope.launch(dispatchersList.io()) {
        captchaRepository.collectCaptchaData{
            if(it.first.isNotEmpty())
                singleUiEventCommunication.map(SingleUiEventState.ShowDialog(CaptchaFragmentDialog()))
        }
    }

    fun sendSingleUiEvent(event: SingleUiEventState) = viewModelScope.launch(dispatchersList.io()) {
        singleUiEventCommunication.map(event)
    }

    fun reloadTracks() = viewModelScope.launch(dispatchersList.io()) {
        downloadCompleteCommunication.map(DownloadResult.Completed)

    }

    fun disablePermissionCheck() = permissionCheckCommunication.map(PermissionCheckState.Empty)


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

     suspend fun collectNotificationBadgeCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<Boolean>,
    ) = notificationsUpdateOnAppStart.collectNotificationBadgeCommunication(owner,collector)

}