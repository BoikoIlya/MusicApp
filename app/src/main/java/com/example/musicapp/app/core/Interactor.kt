package com.example.musicapp.app.core

/**
 * Created by HP on 09.07.2023.
 **/

interface DeleteInteractor<M,E>{

    suspend fun deleteData(id: Int,): E

}

interface AddToFavoritesInteractor<M,E>{

    suspend fun addToFavoritesIfNotDuplicated(item: M): E

    suspend fun addToFavorites(item: M): E
}

interface Interactor<M,E>: AddToFavoritesInteractor<M,E>,DeleteInteractor<M,E> {

    suspend fun containsTrackInDb(item: Pair<String, String>): Boolean

    suspend fun updateData(): String

    suspend fun isDBEmpty(): Boolean

}