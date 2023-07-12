package com.example.musicapp.playlist.domain

import android.util.Log
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.playlist.data.PlaylistDataDomain
import com.example.musicapp.playlist.data.PlaylistRepository
import javax.inject.Inject

/**
 * Created by HP on 22.05.2023.
 **/
interface PlaylistInteractor {

    suspend fun fetchData(): TracksResult
    class Base @Inject constructor(
        private val handleResponse: HandleResponse,
        private val repository: PlaylistRepository,
        private val mapper: PlaylistDataDomain.Mapper<TracksResult>
    ): PlaylistInteractor{

        override suspend fun fetchData(): TracksResult = handleResponse.handle(
            block =  {
                return@handle repository.fetchPlaylists().map(mapper)
            },
            error =  {errorMessage,exception->
                return@handle TracksResult.Failure(errorMessage)
            }
        )

    }
}