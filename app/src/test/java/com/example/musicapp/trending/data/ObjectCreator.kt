package com.example.musicapp.trending.data

import com.example.musicapp.app.dto.*
import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain

/**
 * Created by HP on 28.01.2023.
 **/
abstract class ObjectCreator {

   protected fun getPlaylistsResponse(): Playlists{
        return Playlists(
            meta = Meta(returnedCount = 1, totalCount =1),
            playlists = listOf(Playlist(
                description = "1",
                favoriteCount = 1,
                freePlayCompliant = false,
                href = "1",
                id = "1",
                images = listOf(
                    Image(
                        contentId = "1",
                        height = 1,
                        id = "1",
                        imageType = "1",
                        isDefault = false,
                        type = "1",
                        url = "1",
                        version = 1,
                        width = 1
                    )
                ),
                links = Links(
                    members = Members(
                        href = "1",
                        ids = listOf()
                    ),
                    sampleArtists = SampleArtists(href = "1", ids = listOf()),
                    tags = Tags(href = "1", ids = listOf()),
                    tracks = Tracks(href = "1")
                ),
                modified = "1",
                name = "1",
                privacy = "1",
                trackCount = 1,
                type = "1"
            ))
        )
    }

    fun getTracksResponse(): TracksResponse{
        return TracksResponse(
            meta = MetaX(
                query = Query(
                    limit = 50,
                    next = "1",
                    offset = 1
                ), returnedCount = 1, totalCount = 1
            ), tracks = listOf(
                Track(
                    albumId = "1",
                    albumName = "1",
                    artistId = "1",
                    artistName = "1",
                    blurbs = listOf(),
                    contributors = Contributors(
                        collaborator = "1",
                        composer = "1",
                        engineer = "1",
                        featuredPerformer = "1",
                        guestMusician = "1",
                        guestVocals = "1",
                        primaryArtist = "1",
                        producer = "1",
                        remixer = "1"
                    ),
                    disc = 1,
                    formats = listOf(),
                    href = "1",
                    id = "1",
                    index = 1,
                    isAvailableInAtmos = false,
                    isAvailableInHiRes = false,
                    isExplicit = false,
                    isStreamable = false,
                    isrc = "1",
                    links = LinksX(
                        albums = Albums(
                            href = "1",
                            ids = listOf()
                        ),
                        artists = Artists(href = "1", ids = listOf()),
                        composers = Composers(href = "1", ids = listOf()),
                        genres = Genres(href = "1", ids = listOf()),
                        tags = Tags(href = "1", ids = listOf())
                    ),
                    losslessFormats = listOf(),
                    name = "1",
                    playbackSeconds = 1,
                    previewURL = "1",
                    shortcut = "1",
                    type = "1"
                )
            )
        )
    }

    fun getTrackDomain(): TrackDomain{
        return TrackDomain(
            id = "1",
            playbackMinutes = "0:01",
            name = "1",
            artistName = "1",
            previewURL = "1",
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
}