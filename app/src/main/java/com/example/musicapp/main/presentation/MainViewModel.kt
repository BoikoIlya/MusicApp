package com.example.musicapp.main.presentation

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.UiEventState
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.updatesystem.data.MainViewModelMapper
import com.example.musicapp.updatesystem.data.UpdateSystemRepository
import com.example.musicapp.updatesystem.presentation.FCMUpdateService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by HP on 31.01.2023.
 **/
class MainViewModel @Inject constructor(
    private val playerCommunication: PlayerCommunication,
    private val temporaryTracksCache: TemporaryTracksCache,
    private val dispatchersList: DispatchersList,
    private val singleUiEventCommunication: SingleUiEventCommunication,
    private val bottomSheetCommunication: BottomSheetCommunication,
    private val slideViewPagerCommunication: SlideViewPagerCommunication,
    private val updateSystemRepository: UpdateSystemRepository,
    private val mapper: MainViewModelMapper,
    private val uiEventsCommunication: UiEventsCommunication,
    private val firebaseMessagingWrapper: FirebaseMessagingWrapper
): BaseViewModel(playerCommunication, temporaryTracksCache,dispatchersList),
    CollectPlayerControls{



    init {
       firebaseMessagingWrapper.subscribeToTopic()
        checkForUpdate()
    }

    fun checkForUpdate() = viewModelScope.launch(dispatchersList.io()) {
        updateSystemRepository.checkForNewVersion().map(mapper)
    }
    fun bottomSheetState(state: Int){
        bottomSheetCommunication.map(state)
    }

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

    suspend fun collectSlideViewPagerIndex(
        owner: LifecycleOwner,
        collector: FlowCollector<Int>
    ) = slideViewPagerCommunication.collect(owner,collector)

    suspend fun collectUiEventsCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<UiEventState>
    ) = uiEventsCommunication.collect(owner,collector)


}