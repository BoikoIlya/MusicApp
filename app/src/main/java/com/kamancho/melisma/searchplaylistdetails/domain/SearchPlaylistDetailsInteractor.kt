package com.kamancho.melisma.searchplaylistdetails.domain

import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.HandleResponse
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

    suspend fun fetch(): SearchPlaylistDetailsResult

    suspend fun find(query: String): List<TrackDomain>

     fun initialPlaylistData(): PlaylistDomain

    suspend fun containsCurrentPlaylist(): Boolean

    class Base @Inject constructor(
        private val repository: SearchPlaylistDetailsRepository,
        private val handleResponse: HandleResponse,
        private val playlistCloudToDomainMapper: SearchPlaylistItem.Mapper<PlaylistDomain>,
        private val trackCacheToTrackDomainMapper: TrackCacheToDomainMapper,
        private val transfer: DataTransfer<PlaylistDomain>,
        private val toOwnerIdAndPlaylistIdMapper: PlaylistDomain.Mapper<Pair<Int,String>>,
        private val toTitleMapper: PlaylistDomain.ToTitleMapper
    ):SearchPlaylistDetailsInteractor{

        override suspend fun fetch(): SearchPlaylistDetailsResult = handleResponse.handle({
            val ownerIdAndPlaylistId = transfer.read()!!.map(toOwnerIdAndPlaylistIdMapper)
            val result = repository.fetch(ownerIdAndPlaylistId.second,ownerIdAndPlaylistId.first)
            return@handle SearchPlaylistDetailsResult.Success(
                result.first.map(playlistCloudToDomainMapper),
                result.second.map { trackCacheToTrackDomainMapper.map(it) }
            )
        },{message,_->
            return@handle SearchPlaylistDetailsResult.Error(message)
        })

        override suspend fun find(query: String): List<TrackDomain> =
           repository.find(query).map { trackCacheToTrackDomainMapper.map(it) }


        override  fun initialPlaylistData(): PlaylistDomain = transfer.read()!!

        override suspend fun containsCurrentPlaylist(): Boolean =
            repository.contains(transfer.read()!!.map(toTitleMapper))

    }
}