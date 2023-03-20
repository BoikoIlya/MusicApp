package com.example.musicapp.trending.data

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicapp.app.SpotifyDto.*
import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain
import com.example.testapp.spotifyDto.*

/**
 * Created by HP on 28.01.2023.
 **/
abstract class ObjectCreator {

   protected fun getTracksResponse(): Recomendations {
        return Recomendations(seeds = listOf(), tracks = listOf(Track(
            album = Album(
                album_type = "1",
                artists = listOf(
                    Artist(
                        external_urls = ExternalUrls(spotify = "1"),
                        href = "1",
                        id = "1",
                        name = "1",
                        type = "1",
                        uri = "1"
                    )
                ),
                external_urls = ExternalUrls(spotify = "1"),
                href = "1",
                id = "1",
                images = listOf(Image(height = 1, url = testImgUrl, width = 1)),
                name = "1",
                release_date = "1",
                release_date_precision = "1",
                total_tracks = 1,
                type = "1",
                uri = "1"
            ),
            artists = listOf(
                Artist(
                    external_urls = ExternalUrls(spotify = "1"),
                    href = "1",
                    id = "1",
                    name = "1",
                    type = "1",
                    uri = "1"
                )
            ),
            disc_number = 1,
            duration_ms = 1,
            explicit = false,
            external_ids = ExternalIds(isrc = "1"),
            external_urls = ExternalUrls(spotify = "1"),
            href = "1",
            id = "1",
            is_local = false,
            is_playable = false,
            linked_from = null,
            name = "1",
            popularity = 1,
            preview_url = "https://www.google.by/",
            track_number = 1,
            type = "1",
            uri = "https://www.google.by/"
        ))
        )
    }

    fun getPlaylistsResponse(): FetauredPlaylists {
        return FetauredPlaylists(
            message = "1", playlists = Playlists(
                href = "1",
                items = listOf(Item(
                    collaborative = false,
                    description = "1",
                    external_urls = ExternalUrls(spotify = "1"),
                    href = "1",
                    id = "1",
                    images = listOf(Image(height = 1, url = "1", width = 1)),
                    name = "1",
                    owner = Owner(
                        display_name = "1",
                        external_urls = ExternalUrls(spotify = "1"),
                        href = "1",
                        id = "1",
                        type = "1",
                        uri = "1"
                    ),
                    primary_color = null,
                    public = null,
                    snapshot_id = "1",
                    tracks = Tracks(href = "1", total = 1),
                    type = "1",
                    uri = "1"
                )),
                limit = 1,
                next =1,
                offset = 1,
                previous = "1",
                total = 1
            )
        )
    }

    fun getTrackDomain(): TrackDomain{
        return TrackDomain(
            id = "1",
            imageUrl = testImgUrl,
            name = "1",
            artistName = "1",
            previewURL = "https://www.google.by/",
            albumName = "1"
        )
    }

    fun getPlaylistDomain(): PlaylistDomain{
        return PlaylistDomain(
            id = "1",
            name = "1",
            descriptions = "1",
            imgUrl = "1",
            tracksUrl = "1"
        )
    }

    val testImgUrl = "https://www.google.by/images/branding/googlelogo/1x/googlelogo_light_color_272x92dp.png"
    fun getMediaItem(): MediaItem =
        MediaItem.Builder()
            .setMediaId("https://www.google.by/")
            .setMediaMetadata(MediaMetadata.Builder()
                .setArtworkUri(Uri.parse(testImgUrl))
                .build())
            .build()
}