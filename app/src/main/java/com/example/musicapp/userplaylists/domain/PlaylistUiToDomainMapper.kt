package com.example.musicapp.userplaylists.domain

import com.example.musicapp.app.core.Mapper
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/
interface PlaylistUiToDomainMapper: Mapper<PlaylistUi, PlaylistDomain> {

    class Base @Inject constructor(
        private val mapper: PlaylistUi.Mapper<PlaylistDomain>
    ): PlaylistUiToDomainMapper {

        override fun map(data: PlaylistUi): PlaylistDomain {
            return data.map(mapper)
        }
    }
}