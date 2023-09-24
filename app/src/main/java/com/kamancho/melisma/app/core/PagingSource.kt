package com.kamancho.melisma.app.core

import android.util.Log
import androidx.media3.common.MediaItem
import com.kamancho.melisma.favorites.data.cache.TrackCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by HP on 23.09.2023.
 **/
interface PagingSource<T> {

    suspend fun newPage(
        block: suspend (Int,Int)->Flow<List<T>>

    ): Flow<List<T>>

    fun resetOffset()

   abstract class Abstract<T>(
       private val pageSize:Int
   ) : PagingSource<T>{

        private var offset = 0
        private val totalList = emptyList<T>().toMutableList()
        private var isDbTrigger: Boolean = false
        private var flowTriggerCount:Int =0

        override suspend fun newPage(block: suspend (Int,Int) -> Flow<List<T>>): Flow<List<T>> {
            flowTriggerCount = 0


           return block.invoke(this.pageSize*this.offset,this.pageSize)
                .map {

                    isDbTrigger = flowTriggerCount>=1
                    flowTriggerCount++
                    Log.d("tag", "newPage: ${this.pageSize*this.offset} $pageSize $isDbTrigger")

                   val result =
                       if(isDbTrigger){
                            totalList.clear()
                            block.invoke(0,this.offset*pageSize).first()
                       }
                        else it

                    totalList.addAll(result)

                    val newTotalList = emptyList<T>().toMutableList()
                    newTotalList.addAll(totalList)
                    this.offset++
                    newTotalList

                }

        }

       override fun resetOffset() {
           offset = 0
           totalList.clear()
       }

    }

    class MediaItemsPaging @Inject constructor():
        Abstract<TrackCache>(30),
        PagingSource<TrackCache>
}