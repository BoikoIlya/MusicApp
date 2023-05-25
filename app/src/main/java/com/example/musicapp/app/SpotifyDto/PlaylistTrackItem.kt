package com.example.musicapp.app.SpotifyDto

data class PlaylistTrackItem(
    val added_at: String,
    val added_by: AddedBy,
    val is_local: Boolean,
    val primary_color: Any,
    val track: TrackItemPlaylist,
    val video_thumbnail: VideoThumbnail
)