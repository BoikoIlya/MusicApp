package com.kamancho.melisma.app.core

import com.kamancho.melisma.app.vkdto.SearchPlaylistItem
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 25.10.2023.
 **/

interface PagingSource<T> {


    suspend fun newPage(
        cloud: suspend (Int, Int) -> List<T>,
    ): List<T>

    abstract class Abstract<T>(
        private val pageSize: Int,
    ) : PagingSource<T> {

        protected var offset = 0
        private val totalList = emptyList<T>().toMutableList()
        private var endOfData = false


        override suspend fun newPage(cloud: suspend (Int, Int) -> List<T>): List<T> {
            if (endOfData) return totalList

            val newPage = cloud.invoke(this.pageSize * this.offset, this.pageSize)
            if (newPage.isEmpty()) {
                endOfData = true
                return totalList
            }

            totalList.addAll(newPage)

            val newTotalList = emptyList<T>().toMutableList()
            newTotalList.addAll(totalList)


            this.offset++
            return newTotalList
        }


    }

    class TracksPagingSource @Inject constructor() : Abstract<TrackItem>(pageSize),
        PagingSource<TrackItem> {
        companion object {
            const val pageSize = 20
        }
    }

    class PlaylistsPagingSource @Inject constructor() : Abstract<SearchPlaylistItem>(pageSize),
        PagingSource<SearchPlaylistItem> {
        companion object {
            const val pageSize = 20
        }
    }

}