package com.kamancho.melisma.search.domain

import android.util.Log
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
interface SearchInteractor<T, R> {

    suspend fun load(query: String): SearchDomainState<R>

    abstract class Abstract<T, R>(
        private val handleResponse: HandleResponse,
        private val repository: SearchRepository<T>,
    ) : SearchInteractor<T, R> {

        override suspend fun load(query: String): SearchDomainState<R> = handleResponse.handle({
            val result = repository.search(query)
            return@handle SearchDomainState.Success(map(result))
        }, { message, e ->
            return@handle SearchDomainState.Error(message)
        })

        protected abstract suspend fun map(data: List<T>): List<R>
    }

    class BaseTracks @Inject constructor(
        handleResponse: HandleResponse,
        repository: SearchRepository<TrackItem>,
        private val mapper: TrackItem.Mapper<TrackDomain>,
    ) : SearchInteractor<TrackItem, TrackDomain>,
        Abstract<TrackItem, TrackDomain>(handleResponse, repository) {

        override suspend fun map(data: List<TrackItem>): List<TrackDomain> =
            data.map { it.map(mapper) }
    }


    class BasePlaylists @Inject constructor(
        handleResponse: HandleResponse,
        repository: SearchRepository<SearchPlaylistItem>,
        private val mapper: SearchPlaylistItem.Mapper<PlaylistDomain>,
    ) : SearchInteractor<SearchPlaylistItem, PlaylistDomain>,
        Abstract<SearchPlaylistItem, PlaylistDomain>(handleResponse, repository) {

        override suspend fun map(data: List<SearchPlaylistItem>): List<PlaylistDomain> =
            data.map { it.map(mapper) }
    }


}