package com.kamancho.melisma.artisttracks.domain

import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.artisttracks.data.ArtistTracksRepository
import com.kamancho.melisma.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
interface ArtistsTracksInteractor {

    suspend fun fetchTracks(artistId: String): TracksDomainState

    suspend fun saveTrackId(trackId: String)

    class Base @Inject constructor(
        private val repository: ArtistTracksRepository,
        private val handleResponse: HandleResponse,
        private val managerResource: ManagerResource,
        private val mapper: TrackItem.Mapper<TrackDomain>,
    ) : ArtistsTracksInteractor {

        companion object{
            private const val emptyId: String = "null"
        }

        override suspend fun fetchTracks(artistId: String): TracksDomainState =
            handleResponse.handle(
                block = {
                    if (artistId.isEmpty() || artistId == emptyId)
                        return@handle TracksDomainState.Failure(
                            managerResource.getString(R.string.nothing_found_message),
                            false
                        )

                    val result = repository.fetchTracks(artistId)
                   return@handle if(result.isEmpty()) TracksDomainState.Failure(
                       managerResource.getString(R.string.nothing_found_message),
                       false
                   ) else TracksDomainState.Success(result.map { it.map(mapper) })
                }, error = { message, _ ->
                    return@handle TracksDomainState.Failure(message)
                }
            )

        override suspend fun saveTrackId(trackId: String) {
            val currentTrackId = repository.readTrackId()
            if(trackId!=currentTrackId)
                repository.saveTrackId(trackId)

        }
    }
}