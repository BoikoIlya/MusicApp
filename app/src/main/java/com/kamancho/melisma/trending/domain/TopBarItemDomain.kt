package com.kamancho.melisma.trending.domain

import com.kamancho.melisma.trending.presentation.TrendingTopBarItemUi
import com.kamancho.melisma.trending.presentation.TrendingTopBarNavigationState
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 27.01.2023.
 **/
data class TopBarItemDomain(
    private val id: String,
    private val title: String,
    private val imgUrl: String,
    private val onClickNavigationDestination: TrendingNavigationState,
    private val sortPriority: Int = 0
){

    fun sortPriority() = sortPriority

    interface Mapper<T>{
        fun map(
            id: String,
            title: String,
            imgUrl: String,
            onClickNavigationDestination: TrendingNavigationState,
        ): T
    }

    fun <T>map(mapper: Mapper<T>): T = mapper.map(id,title,imgUrl,onClickNavigationDestination)

    class ToUiMapper @Inject constructor(
        private val mapper: PlaylistDomain.Mapper<PlaylistUi>
    ): Mapper<TrendingTopBarItemUi> {

        override fun map(
            id: String,
            title: String,
            imgUrl: String,
            onClickNavigationDestination: TrendingNavigationState,
        ): TrendingTopBarItemUi {
            return TrendingTopBarItemUi(
                id = id,
                title = title,
                imgUrl = imgUrl,
                navigationState = onClickNavigationDestination.map(mapper)
            )
        }
    }

}

