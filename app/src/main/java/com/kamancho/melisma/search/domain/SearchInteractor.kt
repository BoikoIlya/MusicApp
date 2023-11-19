package com.kamancho.melisma.search.domain

import android.util.Log
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.search.data.SearchRepository
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 25.10.2023.
 **/
interface SearchInteractor<T, S> {

    suspend fun load(query: String): SearchDomainState<S>

    abstract class Abstract<T, S>(
        private val handleResponse: HandleResponse,
        private val repository: SearchRepository<T>,
        private val managerResource: ManagerResource
    ) : SearchInteractor<T, S> {

        override suspend fun load(query: String): SearchDomainState<S> = handleResponse.handle({
            val result = repository.search(query)
            return@handle if(result.isEmpty())
                    SearchDomainState.Error(managerResource.getString(R.string.nothing_found_message))
                else
                    SearchDomainState.Success(map(result))
        }, { message, e ->
            return@handle SearchDomainState.Error(message)
        })

        protected abstract suspend fun map(data: List<T>): List<S>
    }

    class BaseTracks @Inject constructor(
        handleResponse: HandleResponse,
        repository: SearchRepository<TrackItem>,
        private val mapper: TrackItem.Mapper<TrackDomain>,
        managerResource: ManagerResource
    ) : SearchInteractor<TrackItem, TrackDomain>,
        Abstract<TrackItem, TrackDomain>(handleResponse, repository,managerResource) {

        override suspend fun map(data: List<TrackItem>): List<TrackDomain> =
            data.map { it.map(mapper) }
    }


    class BasePlaylists @Inject constructor(
        handleResponse: HandleResponse,
        repository: SearchRepository<SearchPlaylistItem>,
        private val mapper: SearchPlaylistItem.Mapper<PlaylistDomain>,
        managerResource: ManagerResource
    ) : SearchInteractor<SearchPlaylistItem, PlaylistDomain>,
        Abstract<SearchPlaylistItem, PlaylistDomain>(handleResponse, repository,managerResource) {

        override suspend fun map(data: List<SearchPlaylistItem>): List<PlaylistDomain> =
            data.map { it.map(mapper) }
    }


}