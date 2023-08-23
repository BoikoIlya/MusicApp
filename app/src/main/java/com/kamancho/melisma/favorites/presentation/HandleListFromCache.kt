package com.kamancho.melisma.favorites.presentation

/**
 * Created by HP on 15.07.2023.
 **/
interface HandleListFromCache<T> {

   suspend fun handle(list:List<T>)

   abstract class Abstract<T>(
        private val communication: UiCommunication<FavoritesUiState,T>,
    ): HandleListFromCache<T>{

        override suspend fun handle(list:List<T>) {
            if(list.isEmpty()) communication.showUiState(FavoritesUiState.Failure)
            else{
                communication.showUiState(FavoritesUiState.Success)
                communication.showData(list)
            }
        }
    }
}