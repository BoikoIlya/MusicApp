package com.kamancho.melisma.app.core

/**
 * Created by HP on 09.07.2023.
 **/

interface DeleteInteractor<E>{

    suspend fun deleteData(id: Int,): E

}

interface AddToFavoritesInteractor<M,E>{

    suspend fun addToFavoritesIfNotDuplicated(item: M): E

    suspend fun addToFavorites(item: M): E
}

interface UpdateInteractor{

    suspend fun updateData(): String

}

interface Interactor<M,E>: AddToFavoritesInteractor<M,E>,DeleteInteractor<E>, UpdateInteractor {

    suspend fun containsTrackInDb(item: Pair<String, String>): Boolean




}