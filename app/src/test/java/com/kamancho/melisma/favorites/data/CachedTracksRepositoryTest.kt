package com.kamancho.melisma.favorites.data

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.CacheRepository
import com.kamancho.melisma.app.core.ToMediaItemMapper
import com.kamancho.melisma.main.di.AppModule.Companion.mainPlaylistId
import com.kamancho.melisma.trending.data.ObjectCreator
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 28.07.2023.
 **/
class CachedTracksRepositoryTest: ObjectCreator() {

    private lateinit var repository: CacheRepository<MediaItem>
    private lateinit var tracksDao: FavoritesReposotoryTest.TestTracksDao

    @Before
    fun setup(){
        tracksDao = FavoritesReposotoryTest.TestTracksDao()
        repository = CacheRepository.BaseMediaItem(
            dao =tracksDao,
            mapper = ToMediaItemMapper())
    }

    @Test
    fun `test fetch data`() = runBlocking{
        val list = listOf(getTrackCache( name = "b"),getTrackCache(2,"a","b"))
        tracksDao.list.addAll(list)

        TestCase.assertEquals(tracksDao.list[1].trackId.toString(),
            repository.fetch(SortingState.ByName(),mainPlaylistId).first().first().mediaId)

        tracksDao.list.clear()
        tracksDao.list.addAll(listOf(getTrackCache(2, artist =  "b"), getTrackCache(1, artist =  "a")))

        TestCase.assertEquals(tracksDao.list[1].trackId.toString(),
            repository.fetch(SortingState.ByArtist(),mainPlaylistId).first().first().mediaId
        )

        tracksDao.list.clear()
        tracksDao.list.addAll(listOf(getTrackCache(), getTrackCache(2)))

        TestCase.assertEquals(tracksDao.list[1].trackId.toString(),
            repository.fetch(SortingState.ByTime(),mainPlaylistId).first().first().mediaId
        )
    }

}