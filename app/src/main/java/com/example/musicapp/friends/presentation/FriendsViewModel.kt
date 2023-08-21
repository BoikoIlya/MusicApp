package com.example.musicapp.friends.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.FavoritesViewModel
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.favorites.data.cloud.FavoritesService
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.frienddetails.domain.FriendIdAndNameTransfer
import com.example.musicapp.friends.domain.FriendsInteractor
import com.example.musicapp.main.data.cache.AccountDataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

/**
 * Created by HP on 17.08.2023.
 **/
class FriendsViewModel @Inject constructor(
    private val interactor: FriendsInteractor,
    private val communication: FriendsCommunication,
    private val handleFriendsListFromCache: HandleFriendsListFromCache,
    private val handleFriendsUpdate: HandleFriendsUpdate,
    private val dispatchersList: DispatchersList,
    private val transfer: FriendIdAndNameTransfer
): ViewModel(), FavoritesViewModel<FavoritesUiState,FriendUi> {

    init {
        update(false)
        search("")
    }

    override fun update(loading: Boolean) {
        viewModelScope.launch{
            handleFriendsUpdate.handle(loading){ interactor.search("").first().isEmpty() }
        }
    }

    private val searchJob: Job?=null

    fun search(query: String) = viewModelScope.launch(dispatchersList.io()){
        searchJob?.cancel()

        interactor.search(query).collectLatest {list->
            handleFriendsListFromCache.handle(list.map { it.map() })
        }
    }

    fun saveFriendData(data: Pair<String,String>){
        transfer.save(data)
    }

    override suspend fun collectState(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoritesUiState>,
    ) = communication.collectState(owner,collector)

    override suspend fun collectData(
        owner: LifecycleOwner,
        collector: FlowCollector<List<FriendUi>>,
    ) = communication.collectData(owner,collector)

    override suspend fun collectLoading(
        owner: LifecycleOwner,
        collector: FlowCollector<FavoritesUiState>,
    ) =  communication.collectLoading(owner,collector)


}