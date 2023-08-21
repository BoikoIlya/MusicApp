package com.example.musicapp.hlscachesystem.data

import androidx.media3.common.MediaItem
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject

/**
 * Created by HP on 08.08.2023.
 **/
interface HlsCacheQueueStore {

   suspend fun add(mediaItem: MediaItem)

    suspend fun mediaItem(): MediaItem?

    class Base @Inject constructor(): HlsCacheQueueStore {
        private val queue: Queue<MediaItem> = LinkedList()


        override suspend fun add(mediaItem: MediaItem) {
            synchronized(queue) {
                queue.add(mediaItem)
            }
        }

        override suspend fun mediaItem(): MediaItem? =  synchronized(queue) { queue.poll()}
    }
}