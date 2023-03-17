package com.example.musicapp.main.domain

import androidx.media3.common.MediaItem
import com.example.musicapp.app.main.presentation.PlayerCommunication
import com.example.musicapp.app.main.presentation.PlayerCommunicationState

/**
 * Created by HP on 15.03.2023.
 **/
class QueryResultMapper(
    private val playerCommunication: PlayerCommunication
    ): QueryResult.Mapper<Unit> {

    override fun map(list: List<MediaItem>, errorMessage: String) {
        playerCommunication.map(PlayerCommunicationState.SetQuery(list))
    }
}