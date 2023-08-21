package com.example.musicapp.trending.domain

import com.example.musicapp.trending.data.TrendingRepository.Base.Companion.emptyDestination
import com.example.musicapp.trending.presentation.TrendingTopBarItemUi
import com.example.musicapp.trending.presentation.TrendingTopBarNavigationState
import javax.inject.Inject

/**
 * Created by HP on 27.01.2023.
 **/
data class TopBarItemDomain(
    private val id: String,
    private val title: String,
    private val imgUrl: String,
    private val onClickNavigationDestination: Int,
    private val sortPriority: Int
){

    fun sortPriority() = sortPriority

    interface Mapper<T>{
        fun map(
            id: String,
            title: String,
            imgUrl: String,
            onClickNavigationDestination: Int,
        ): T
    }

    fun <T>map(mapper: Mapper<T>): T = mapper.map(id,title,imgUrl,onClickNavigationDestination)

    class ToUiMapper @Inject constructor(): Mapper<TrendingTopBarItemUi> {

        override fun map(
            id: String,
            title: String,
            imgUrl: String,
            onClickNavigationDestination: Int,
        ): TrendingTopBarItemUi {
            return TrendingTopBarItemUi(
                id = id,
                title = title,
                imgUrl = imgUrl,
                navigationState =
                if(onClickNavigationDestination==emptyDestination) TrendingTopBarNavigationState.Empty
                else TrendingTopBarNavigationState.Navigate(onClickNavigationDestination),
            )
        }
    }

}

