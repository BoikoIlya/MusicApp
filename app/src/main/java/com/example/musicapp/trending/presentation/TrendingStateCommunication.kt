package com.example.musicapp.trending.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface TrendingStateCommunication: Communication.Mutable<TracksUiState>{
    class Base @Inject constructor():
        Communication.UiUpdate<TracksUiState>(TracksUiState.Loading),
        TrendingStateCommunication
}