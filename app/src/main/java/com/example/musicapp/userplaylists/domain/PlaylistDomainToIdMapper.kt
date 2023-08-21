package com.example.musicapp.userplaylists.domain

import com.example.musicapp.app.core.Mapper
import javax.inject.Inject

/**
 * Created by HP on 12.07.2023.
 **/
interface PlaylistDomainToIdMapper: Mapper<PlaylistDomain,Pair<Int,String>> {

    class Base @Inject constructor(
        private val mapper: PlaylistDomain.Mapper<Pair<Int,String>>
    ): PlaylistDomainToIdMapper{

        override fun map(data: PlaylistDomain): Pair<Int,String> = data.map(mapper)
    }
}

interface PlaylistDomainToIdMapperInt: Mapper<PlaylistDomain,Pair<Int,Int>> {

    class Base @Inject constructor(
        private val mapper: PlaylistDomain.Mapper<Pair<Int,Int>>
    ): PlaylistDomainToIdMapperInt{

        override fun map(data: PlaylistDomain): Pair<Int,Int> = data.map(mapper)
    }
}