package com.kamancho.melisma.userplaylists.domain

import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
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