package com.kamancho.melisma.trending.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface TrendingStateCommunication: Communication.Mutable<TrendingUiState>{
    class Base @Inject constructor():
        Communication.UiUpdate<TrendingUiState>(TrendingUiState.Loading),
        TrendingStateCommunication
}