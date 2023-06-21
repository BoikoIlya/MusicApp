package com.example.musicapp.favorites.domain

import android.util.Log
import com.example.musicapp.app.core.HandleError
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.core.TracksRepository
import com.example.musicapp.favorites.data.FavoriteTracksRepository
import javax.inject.Inject

/**
 * Created by HP on 17.06.2023.
 **/
interface FavoritesInteractor {

    suspend fun updateData(): String

    class Base @Inject constructor(
        private val repository: FavoriteTracksRepository,
        private val handleError: HandleError
    ): FavoritesInteractor{

        override suspend fun updateData(): String {

            return try {
                repository.updateData()
                ""
            }catch (e:Exception){
                Log.d("tag", "updateData: $e")
                handleError.handle(e)

            }

        }

    }
}