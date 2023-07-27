package com.example.musicapp.userplaylists.domain

import com.example.musicapp.app.core.Mapper
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/
interface PlaylistDomainToContainsMapper: Mapper<PlaylistDomain, Pair<String,String>> {

    class Base @Inject constructor(
        private val mapper: PlaylistDomain.Mapper<Pair<String,String>>
    ): PlaylistDomainToContainsMapper{

        override fun map(data: PlaylistDomain): Pair<String, String> {
            return data.map(mapper)
        }


    }
}