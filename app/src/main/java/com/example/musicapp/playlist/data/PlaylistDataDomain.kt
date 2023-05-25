package com.example.musicapp.playlist.data

import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
data class PlaylistDataDomain(
    private val tracks: List<TrackDomain>,
    private val albumDescription: String,
    private val albumName: String,
    private val albumImgUrl: String
) {

    fun <T>map(mapper: Mapper<T>):T = mapper.map(tracks, albumDescription, albumName, albumImgUrl)

    interface Mapper<T>{

        fun map(
            tracks: List<TrackDomain>,
            albumDescription: String,
            albumName: String,
            albumImgUrl: String
        ): T
    }

    class ToTrackResultMapper @Inject constructor(
        private val mapper: TrackDomain.Mapper<MediaItem>
    ):  Mapper<TracksResult> {

        override fun map(
            tracks: List<TrackDomain>,
            albumDescription: String,
            albumName: String,
            albumImgUrl: String,
        ): TracksResult {
            return TracksResult.SuccessAlbumTracks(
                tracks.map { it.map(mapper) },
                albumDescription,
                albumName,
                albumImgUrl
            )
        }
    }

}
