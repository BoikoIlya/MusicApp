package com.example.musicapp.trending.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface TrendingPlaylistsCommunication: Communication.Mutable<List<PlaylistUi>>{
    class Base @Inject constructor():
        Communication.UiUpdate<List<PlaylistUi>>(emptyList()),
        TrendingPlaylistsCommunication
}