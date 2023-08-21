package com.example.musicapp.searchplaylistdetails.presentation

import android.util.Log
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.userplaylists.domain.PlaylistsResult
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/
//
//interface PlaylistsResultFollowToUiEventMapper: PlaylistsResult.Mapper<Unit> {
//
//
//    class Base @Inject constructor(
//        private val globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
//        private val search
//    ) : PlaylistsResultFollowToUiEventMapper {
//
//        override suspend fun map(message: String, error: Boolean) {
//            globalSingleUiEventCommunication.map(
//                if(error)
//                    SingleUiEventState.ShowSnackBar.Error(message)
//                else
//                    SingleUiEventState.ShowSnackBar.Success(message))
//
//        }
//    }
//}