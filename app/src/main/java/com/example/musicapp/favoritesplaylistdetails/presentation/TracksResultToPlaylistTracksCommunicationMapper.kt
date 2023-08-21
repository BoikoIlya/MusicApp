package com.example.musicapp.favoritesplaylistdetails.presentation

/**
 * Created by HP on 23.05.2023.
 **/

//interface TracksResultToPlaylistTracksCommunicationMapper:
//    TracksResultToTracksCommunicationMapper<TracksUiState> {
//
//    class Base @Inject constructor(
//        private val communication: PlaylistDetailsCommunication,
//        ):
//        TracksResultToPlaylistTracksCommunicationMapper,
//        TracksResultToTracksCommunicationMapper.Abstract<TracksUiState>(communication) {
//
//        override suspend fun map(
//            message: String,
//            list: List<MediaItem>,
//            albumDescription: String,
//            albumName: String,
//            albumImgUrl: String
//        ) {
//            communication.showAdditionalPlaylistInfo(Triple(albumName,albumImgUrl,albumDescription))
//            super.map(message,list,false,-1)
//        }
//
//        override fun showError(message: String): TracksUiState = TracksUiState.Error(message)
//        override fun showSuccess(): TracksUiState = TracksUiState.Success
//    }
//}