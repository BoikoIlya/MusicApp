package com.example.musicapp.trending.data

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicapp.app.SpotifyDto.*
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.playlist.data.PlaylistDataDomain
import com.example.musicapp.searchhistory.data.cache.HistoryItemCache
import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain
import com.example.testapp.spotifyDto.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

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
            imgUrl = "1",
            tracksUrl = "1"
        )
    }

    val testImgUrl = "https://www.google.by/images/branding/googlelogo/1x/googlelogo_light_color_272x92dp.png"
    fun getMediaItem(id: String="1", name: String="", artist: String=""): MediaItem =
        MediaItem.Builder()
            .setMediaId(id)
            .setMediaMetadata(MediaMetadata.Builder()
                .setTitle(name)
                .setArtist(artist)
                .setArtworkUri(Uri.parse(testImgUrl))
                .build())
            .build()


    fun getTrackCache(id: String="1", name: String="", artist: String=""): TrackCache =
        TrackCache(
            id = id,
            name = name,
            artistName = artist,
            imgUrl = "",
            albumName = "",
            time = Calendar.getInstance().timeInMillis
        )

    fun getHisstoryItem(query: String="", time: Long =0) = HistoryItemCache(query, time)

    fun getSearchDto() = SearchDto(SearchTracks("", listOf(
        SearchItem(
            album = Album(
                album_type = "1",
                artists = listOf(),
                external_urls = ExternalUrls(spotify = "1"),
                href = "1",
                id = "1",
                images = listOf(),
                name = "1",
                release_date = "1",
                release_date_precision = "1",
                total_tracks = 1,
                type = "1",
                uri = "1"
            ),
            artists = listOf(),
            available_markets = listOf(),
            disc_number = 1,
            duration_ms = 1,
            explicit = false,
            external_ids = ExternalIds(isrc = ""),
            external_urls = ExternalUrls(spotify = ""),
            href = "1",
            id = "1",
            is_local = false,
            name = "1",
            popularity = 0,
            preview_url = "1",
            track_number = 0,
            type = "1",
            uri = "1"
        )
    ),1,"1",1,1,1))

    fun getPlaylistDto() = PlaylistDto(
        collaborative = false,
        description = "1",
        external_urls = ExternalUrls(spotify = "1"),
        followers = Followers(href = "1", total = 0),
        href = "1",
        id = "1",
        images = listOf(Image(1,"1",1)),
        name = "1",
        owner = Owner(
            display_name = "",
            external_urls = ExternalUrls(spotify = ""),
            href = "",
            id = "",
            type = "",
            uri = ""
        ),
        primary_color =1,
        public = false,
        snapshot_id = "",
        tracks = PlaylistTracks(
            href = "1",
            items = listOf(
                PlaylistTrackItem(
                    added_at = "", added_by = AddedBy(
                        external_urls = ExternalUrls(
                            spotify = ""
                        ), href = "", id = "", type = "", uri = ""
                    ), is_local = false, primary_color =1, track = TrackItemPlaylist(
                        album = AlbumPlaylist(
                            album_type = "",
                            artists = listOf(
                                Artist(
                                    external_urls = ExternalUrls(spotify = ""),
                                    href = "",
                                    id = "",
                                    name = "1",
                                    type = "",
                                    uri = ""
                                )
                            ),
                            available_markets = listOf(),
                            external_urls = ExternalUrls(spotify = ""),
                            href = "",
                            id = "1",
                            images = listOf(Image(1,testImgUrl,1)),
                            name = "1",
                            release_date = "",
                            release_date_precision = "",
                            total_tracks = 0,
                            type = "",
                            uri = "1"
                        ),
                        artists = listOf(
                            Artist(
                                external_urls = ExternalUrls(spotify = ""),
                                href = "",
                                id = "",
                                name = "1",
                                type = "",
                                uri = ""
                            )
                        ),
                        available_markets = listOf(),
                        disc_number = 0,
                        duration_ms = 0,
                        episode = false,
                        explicit = false,
                        external_ids = ExternalIds(isrc = ""),
                        external_urls = ExternalUrls(spotify = ""),
                        href = "",
                        id = "1",
                        is_local = false,
                        name = "1",
                        popularity = 0,
                        preview_url = "https://www.google.by/",
                        track = false,
                        track_number = 0,
                        type = "",
                        uri = ""
                    ), video_thumbnail = VideoThumbnail(url =1)
                )
            ),
            limit = 1,
            next ="",
            offset = 0,
            previous =1,
            total = 0
        ),
        type = "1",
        uri = "1"
    )

    fun getPlaylistDataDomain() = PlaylistDataDomain(
        tracks = listOf(getTrackDomain()), albumDescription = "1", albumName = "1", albumImgUrl = "1"

    )
}