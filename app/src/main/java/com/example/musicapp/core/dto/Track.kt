package com.example.musicapp.core.dto

import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain
import java.util.concurrent.TimeUnit

data class Track(
    val albumId: String,
    val albumName: String,
    val artistId: String,
    val artistName: String,
    val blurbs: List<Any>,
    val contributors: Contributors,
    val disc: Int,
    val formats: List<Format>,
    val href: String,
    val id: String,
    val index: Int,
    val isAvailableInAtmos: Boolean,
    val isAvailableInHiRes: Boolean,
    val isExplicit: Boolean,
    val isStreamable: Boolean,
    val isrc: String,
    val links: LinksX,
    val losslessFormats: List<LosslessFormat>,
    val name: String,
    val playbackSeconds: Int,
    val previewURL: String,
    val shortcut: String,
    val type: String
){
    interface Mapper<T> {

        fun map(
            albumId: String,
            albumName: String,
            artistId: String,
            artistName: String,
            blurbs: List<Any>,
            contributors: Contributors,
            disc: Int,
            formats: List<Format>,
            href: String,
            id: String,
            index: Int,
            isAvailableInAtmos: Boolean,
            isAvailableInHiRes: Boolean,
            isExplicit: Boolean,
            isStreamable: Boolean,
            isrc: String,
            links: LinksX,
            losslessFormats: List<LosslessFormat>,
            name: String,
            playbackSeconds: Int,
            previewURL: String,
            shortcut: String,
            type: String
        ): T
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(
        albumId,
        albumName,
        artistId,
        artistName,
        blurbs,
        contributors,
        disc,
        formats,
        href,
        id,
        index,
        isAvailableInAtmos,
        isAvailableInHiRes,
        isExplicit,
        isStreamable,
        isrc,
        links,
        losslessFormats,
        name,
        playbackSeconds,
        previewURL,
        shortcut,
        type
    )


    class ToTrackDomain : Mapper<TrackDomain> {
        override fun map(
            albumId: String,
            albumName: String,
            artistId: String,
            artistName: String,
            blurbs: List<Any>,
            contributors: Contributors,
            disc: Int,
            formats: List<Format>,
            href: String,
            id: String,
            index: Int,
            isAvailableInAtmos: Boolean,
            isAvailableInHiRes: Boolean,
            isExplicit: Boolean,
            isStreamable: Boolean,
            isrc: String,
            links: LinksX,
            losslessFormats: List<LosslessFormat>,
            name: String,
            playbackSeconds: Int,
            previewURL: String,
            shortcut: String,
            type: String,
        ): TrackDomain {
            var timeResult =""
            val min = TimeUnit.SECONDS.toMinutes(playbackSeconds.toLong())
            val sec = playbackSeconds - min*60
            timeResult = if(sec<10) "$min:0$sec" else "$min:$sec"
            return TrackDomain(
                id = id,
                playbackMinutes = timeResult,
                name = name,
                artistName = artistName,
                previewURL = previewURL,
                albumName = albumName
            )
        }


    }
}