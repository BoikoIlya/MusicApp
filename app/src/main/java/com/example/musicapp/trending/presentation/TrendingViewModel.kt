package com.example.musicapp.trending.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.trending.di.TrendingComponent
import com.example.musicapp.trending.domain.TrendingInteractor
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 26.01.2023.
 **/
class TrendingViewModel @Inject constructor(
    private val interactor: TrendingInteractor,
    private val trendingCommunication: TrendingCommunication,
    private val handleTrendingResult: HandleTrendingResult
): ViewModel(), CollectTrendings {


    init {
          loadData()
    }

    fun loadData() = handleTrendingResult.handle(viewModelScope){
        interactor.fetchData()
    }



    override suspend fun collectPlaylists(
        owner: LifecycleOwner,
        collector: FlowCollector<List<PlaylistUi>>
    ) = trendingCommunication.collectPlaylists(owner, collector)

    override suspend fun collectState(
        owner: LifecycleOwner,
        collector: FlowCollector<TrendingUiState>
    ) = trendingCommunication.collectState(owner, collector)

    override suspend fun collectTracks(
        owner: LifecycleOwner,
        collector: FlowCollector<List<TrackUi>>
    ) = trendingCommunication.collectTracks(owner,collector)

}