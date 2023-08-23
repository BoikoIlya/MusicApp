package com.kamancho.melisma.creteplaylist.data.cloud

/**
 * Created by HP on 18.07.2023.
 **/
data class AddToPlaylistResponse(
    private val response: List<AudioId>
)

data class AudioId(val audioId: Int)