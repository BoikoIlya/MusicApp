package com.example.musicapp.popular.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.Communication
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.favorites.presentation.UiCommunication
import javax.inject.Inject

/**
 * Created by HP on 22.08.2023.
 **/
interface PopularCommunication: UiCommunication<FavoritesUiState, MediaItem> {


    class Base @Inject constructor(
        popularListCommunication: PopularListCommunication,
        popularUiStateCommunication: PopularUiStateCommunication,
    ): PopularCommunication,
        UiCommunication.Abstract<FavoritesUiState, MediaItem>(
            stateCommunication = popularUiStateCommunication,
            tracksCommunication = popularListCommunication
        )

}

interface PopularListCommunication: Communication.Mutable<List<MediaItem>>{

    class Base @Inject constructor(): PopularListCommunication, Communication.UiUpdate<List<MediaItem>>(emptyList())
}

interface PopularUiStateCommunication: Communication.Mutable<FavoritesUiState>{

    class Base @Inject constructor(): PopularUiStateCommunication, Communication.UiUpdate<FavoritesUiState>(FavoritesUiState.Empty)
}
