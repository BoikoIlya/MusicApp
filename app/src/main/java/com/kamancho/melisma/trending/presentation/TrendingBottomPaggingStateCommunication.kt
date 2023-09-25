package com.kamancho.melisma.trending.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface TrendingBottomPaggingStateCommunication: Communication.Mutable<TrendingBottomPagingState>{
    class Base @Inject constructor():
        Communication.UiUpdate<TrendingBottomPagingState>(TrendingBottomPagingState.DisableLoadingBottom),
        TrendingBottomPaggingStateCommunication
}