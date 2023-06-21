package com.example.musicapp.app.vkdto

data class Item(
    val access_key: String,
    val ads: Ads,
    val album: Album?=null,
    val artist: String,
    val content_restricted: Int,
    val date: Int,
    val duration: Int,
    val featured_artists: List<FeaturedArtist>,
    val genre_id: Int,
    val id: Int,
    val is_explicit: Boolean,
    val is_focus_track: Boolean,
    val is_licensed: Boolean,
    val lyrics_id: Int,
    val main_artists: List<MainArtist>,
    val no_search: Int,
    val owner_id: Int,
    val short_videos_allowed: Boolean,
    val stories_allowed: Boolean,
    val stories_cover_allowed: Boolean,
    val subtitle: String,
    val title: String,
    val track_code: String,
    val url: String
)