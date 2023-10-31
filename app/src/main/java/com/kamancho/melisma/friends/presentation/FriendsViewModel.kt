package com.kamancho.melisma.friends.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.FavoritesViewModel
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import com.kamancho.melisma.friends.domain.FriendsInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 17.08.2023.
 **/
class FriendsViewModel @Inject constructor(
    private val interactor: FriendsInteractor,
    private val communication: FriendsCommunication,
    private val handleFriendsListFromCache: HandleFriendsListFromCache,
    private val handleFriendsUpdate: HandleFriendsUpdate,
    private val dispatchersList: DispatchersList,
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