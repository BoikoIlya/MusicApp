package com.kamancho.melisma.searchplaylistdetails.domain

import android.util.Log
import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.VkException
import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.searchplaylistdetails.data.SearchPlaylistDetailsRepository
import com.kamancho.melisma.searchplaylistdetails.data.TrackCacheToDomainMapper
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.trending.presentation.SearchPlaylistDetailsResult
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import javax.inject.Inject

/**
 * Created by HP on 15.08.2023.
 **/
interface SearchPlaylistDetailsInteractor {

    suspend fun fetch(ownerId: Int,playlistId: String): SearchPlaylistDetailsResult

    suspend fun find(query: String): List<TrackDomain>


    suspend fun containsCurrentPlaylist(title: String): Boolean

    class Base @Inject constructor(
        private val repository: SearchPlaylistDetailsRepository,
        private val handleResponse: HandleResponse,
        private val playlistCloudToDomainMapper: SearchPlaylistItem.Mapper<PlaylistDomain>,
        private val trackCacheToTrackDomainMapper: TrackCacheToDomainMapper,
    ):SearchPlaylistDetailsInteractor{

        override suspend fun fetch(ownerId: Int,playlistId: String): SearchPlaylistDetailsResult = handleResponse.handle({
            val result = repository.fetch(playlistId,ownerId)

            return@handle SearchPlaylistDetailsResult.Success(
                result.first.map(playlistCloudToDomainMapper),
                result.second.map { trackCacheToTrackDomainMapper.map(it) }
            )
        },{message,e->
            return@handle SearchPlaylistDetailsResult.Error(message)
        })

        override suspend fun find(query: String): List<TrackDomain> =
           repository.find(query).map { trackCacheToTrackDomainMapper.map(it) }



        override suspend fun containsCurrentPlaylist(title:String): Boolean =
            repository.contains(title)

    }
}