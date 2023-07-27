package com.example.musicapp.trending.data


import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicapp.app.core.ToMediaItemMapper
import com.example.musicapp.app.vkdto.Ads
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.app.vkdto.TracksResponse
import com.example.musicapp.app.vkdto.TracksCloud
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.searchhistory.data.cache.HistoryItemCache
import com.example.musicapp.trending.domain.TrackDomain
import org.mockito.Mockito.mock
import java.time.Instant


/**
 * Created by HP on 28.01.2023.
 **/
abstract class ObjectCreator {
//
//   protected fun getTracksResponse(): Recomendations {
//        return Recomendations(seeds = listOf(), tracks = listOf(Track(
//            album = Album(
//                album_type = "1",
//                artists = listOf(
//                    Artist(
//                        external_urls = ExternalUrls(spotify = "1"),
//                        href = "1",
//                        id = "1",
//                        name = "1",
//                        type = "1",
//                        uri = "1"
//                    )
//                ),
//                external_urls = ExternalUrls(spotify = "1"),
//                href = "1",
//                id = "1",
//                images = listOf(Image(height = 1, url = testImgUrl, width = 1)),
//                name = "1",
//                release_date = "1",
//                release_date_precision = "1",
//                total_tracks = 1,
//                type = "1",
//                uri = "1"
//            ),
//            artists = listOf(
//                Artist(
//                    external_urls = ExternalUrls(spotify = "1"),
//                    href = "1",
//                    id = "1",
//                    name = "1",
//                    type = "1",
//                    uri = "1"
//                )
//            ),
//            disc_number = 1,
//            duration_ms = 1,
//            explicit = false,
//            external_ids = ExternalIds(isrc = "1"),
//            external_urls = ExternalUrls(spotify = "1"),
//            href = "1",
//            id = "1",
//            is_local = false,
//            is_playable = false,
//            linked_from = null,
//            name = "1",
//            popularity = 1,
//            preview_url = "https://www.google.by/",
//            track_number = 1,
//            type = "1",
//            uri = "https://www.google.by/"
//        ))
//        )
//    }
//
//    fun getPlaylistsResponse(): FetauredPlaylists {
//        return FetauredPlaylists(
//            message = "1", playlists = Playlists(
//                href = "1",
//                items = listOf(Item(
//                    collaborative = false,
//                    description = "1",
//                    external_urls = ExternalUrls(spotify = "1"),
//                    href = "1",
//                    id = "1",
//                    images = listOf(Image(height = 1, url = "1", width = 1)),
//                    name = "1",
//                    owner = Owner(
//                        display_name = "1",
//                        external_urls = ExternalUrls(spotify = "1"),
//                        href = "1",
//                        id = "1",
//                        type = "1",
//                        uri = "1"
//                    ),
//                    primary_color = null,
//                    public = null,
//                    snapshot_id = "1",
//                    tracks = Tracks(href = "1", total = 1),
//                    type = "1",
//                    uri = "1"
//                )),
//                limit = 1,
//                next =1,
//                offset = 1,
//                previous = "1",
//                total = 1
//            )
//        )
//    }
//
    fun getTrackDomain(id: Int =1,name: String ="1", author: String= "1"): TrackDomain{
        return TrackDomain(
            id = id,
            smallImgUrl = testImgUrl,
            name = name,
            artistName = author,
            albumName = "1",
            track_url = "https://www.google.by/",
            bigImgUrl = "",
            date = 0,
            durationInSeconds = 0,
            ownerId = 0
        )
    }
//
//    fun getPlaylistDomain(): PlaylistDomain{
//        return PlaylistDomain(
//            id = "1",
//            name = "1",
//            imgUrl = "1",
//            tracksUrl = "1"
//        )
//    }
//
val testImgUrl = "https://www.google.by/images/branding/googlelogo/1x/googlelogo_light_color_272x92dp.png"
    fun getMediaItem(id: String = "1", name: String = "", artist: String = ""): MediaItem {
        val extras: Bundle = mock(Bundle::class.java)
        extras.putInt(ToMediaItemMapper.track_id, 1)
        extras.putString(ToMediaItemMapper.small_img_url, "1")
        extras.putString(ToMediaItemMapper.big_img_url, "1")
        extras.putFloat(ToMediaItemMapper.track_duration_in_millis, 1.3f)
        extras.putInt(ToMediaItemMapper.owner_id, 1)

        return MediaItem.Builder()
            .setMediaId(testImgUrl)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(name)
                    .setArtist(artist)
                    .setExtras(extras)
                    .build()
            )
            .build()
    }




    fun getTrackCache(id: Int=1, name: String="", artist: String=""): TrackCache =
        TrackCache(
            id = id,
            name = name,
            artistName = artist,
            albumName = "",
            date = Instant.now().toEpochMilli().toInt(),
            url = "1",
            bigImgUrl = "1",
            smallImgUrl = "1",
            durationFormatted = "1",
            durationInMillis = 1.0f,
            ownerId = 1,

        )
//
    fun getHisstoryItem(query: String="", time: Long =0) = HistoryItemCache(query, time)
//
    fun getTracksCloud() =  TracksCloud(
    response = TracksResponse(
        count = 1,
        items = listOf(TrackItem(
            access_key = "1",
            ads = Ads(
                account_age_type = "1",
                content_id = "1",
                duration = "1",
                puid22 = "1"
            ),
            album = null,
            artist = "1",
            content_restricted = 0,
            date = 0,
            duration = 0,
            featured_artists = listOf(),
            genre_id = 0,
            id = 0,
            is_explicit = false,
            is_focus_track = false,
            is_licensed = false,
            lyrics_id = 0,
            main_artists = listOf(),
            no_search = 0,
            owner_id = 0,
            short_videos_allowed = false,
            stories_allowed = false,
            stories_cover_allowed = false,
            subtitle = null,
            title = "1",
            track_code = "1",
            url = "1"
        ))
    ))
//
//    fun getPlaylistDto() = PlaylistDto(
//        collaborative = false,
//        description = "1",
//        external_urls = ExternalUrls(spotify = "1"),
//        followers = Followers(href = "1", total = 0),
//        href = "1",
//        id = "1",
//        images = listOf(Image(1,"1",1)),
//        name = "1",
//        owner = Owner(
//            display_name = "",
//            external_urls = ExternalUrls(spotify = ""),
//            href = "",
//            id = "",
//            type = "",
//            uri = ""
//        ),
//        primary_color =1,
//        public = false,
//        snapshot_id = "",
//        tracks = PlaylistTracks(
//            href = "1",
//            items = listOf(
//                PlaylistTrackItem(
//                    added_at = "", added_by = AddedBy(
//                        external_urls = ExternalUrls(
//                            spotify = ""
//                        ), href = "", id = "", type = "", uri = ""
//                    ), is_local = false, primary_color =1, track = TrackItemPlaylist(
//                        album = AlbumPlaylist(
//                            album_type = "",
//                            artists = listOf(
//                                Artist(
//                                    external_urls = ExternalUrls(spotify = ""),
//                                    href = "",
//                                    id = "",
//                                    name = "1",
//                                    type = "",
//                                    uri = ""
//                                )
//                            ),
//                            available_markets = listOf(),
//                            external_urls = ExternalUrls(spotify = ""),
//                            href = "",
//                            id = "1",
//                            images = listOf(Image(1,testImgUrl,1)),
//                            name = "1",
//                            release_date = "",
//                            release_date_precision = "",
//                            total_tracks = 0,
//                            type = "",
//                            uri = "1"
//                        ),
//                        artists = listOf(
//                            Artist(
//                                external_urls = ExternalUrls(spotify = ""),
//                                href = "",
//                                id = "",
//                                name = "1",
//                                type = "",
//                                uri = ""
//                            )
//                        ),
//                        available_markets = listOf(),
//                        disc_number = 0,
//                        duration_ms = 0,
//                        episode = false,
//                        explicit = false,
//                        external_ids = ExternalIds(isrc = ""),
//                        external_urls = ExternalUrls(spotify = ""),
//                        href = "",
//                        id = "1",
//                        is_local = false,
//                        name = "1",
//                        popularity = 0,
//                        preview_url = "https://www.google.by/",
//                        track = false,
//                        track_number = 0,
//                        type = "",
//                        uri = ""
//                    ), video_thumbnail = VideoThumbnail(url =1)
//                )
//            ),
//            limit = 1,
//            next ="",
//            offset = 0,
//            previous =1,
//            total = 0
//        ),
//        type = "1",
//        uri = "1"
//    )
//
//    fun getPlaylistDataDomain() = PlaylistDataDomain(
//        tracks = listOf(getTrackDomain()), albumDescription = "1", albumName = "1", albumImgUrl = "1"
//
//    )
}