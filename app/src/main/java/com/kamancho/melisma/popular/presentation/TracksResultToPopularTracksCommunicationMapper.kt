package com.kamancho.melisma.popular.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import com.kamancho.melisma.favorites.presentation.TracksResult
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface TracksResultToPopularTracksCommunicationMapper: TracksResult.Mapper<Unit>{


    class Base @Inject constructor(
        private val communication: PopularCommunication,
        private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication
    ): TracksResultToPopularTracksCommunicationMapper {

        override suspend fun map(
            message: String,
            list: List<MediaItem>,
            error: Boolean,
            newId: Int,
        ) {
             if(list.isNotEmpty()) {
                communication.showUiState(FavoritesUiState.Success)
                communication.showData(list)

            }else globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(message))
            communication.showUiState(FavoritesUiState.DisableLoading)
        }


    }
}