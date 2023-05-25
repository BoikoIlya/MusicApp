package com.example.musicapp.favorites.data

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.ToMediaItemMapper
import com.example.musicapp.app.core.ToTrackCacheMapper
import com.example.musicapp.favorites.testcore.TestManagerResource
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.data.cache.TracksDao
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.trending.data.ObjectCreator
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by HP on 28.04.2023.
 **/
class FavoritesReposotoryTest: ObjectCreator() {

    lateinit var repository: FavoriteTracksRepository
    lateinit var tracksDao: TestTracksDao
    lateinit var managerResource: ManagerResource

    @Before
    fun setup(){
        tracksDao = TestTracksDao()
        managerResource = TestManagerResource()
        repository = FavoriteTracksRepository.Base(
            cache = tracksDao,
            toCacheMapper = ToTrackCacheMapper(),
            transfer = DataTransfer.MusicDialogTransfer(),
            toMediaItemMapper = ToMediaItemMapper(),
            managerResource = managerResource
        )
    }


    @Test
    fun `test fetch data`() = runBlocking{
        val list = listOf(getMediaItem(name = "a"), getMediaItem("2", name = "b")).toMutableList()
        tracksDao.list.addAll(listOf(getTrackCache("2",name = "b"), getTrackCache( name = "a")))

        assertEquals(true,
            repository.fetchData(SortingState.ByName()).first().map(TestTracksResultMapper(list.sortedBy { it.mediaMetadata.title.toString() }))
            )

        tracksDao.list.clear()
        tracksDao.list.addAll(listOf(getTrackCache("2",artist = "b"), getTrackCache( artist = "a")))

        assertEquals(true,
            repository.fetchData(SortingState.ByArtist()).first().map(TestTracksResultMapper(list.sortedBy { it.mediaMetadata.artist.toString() }))
        )

        tracksDao.list.clear()
        tracksDao.list.addAll(listOf(getTrackCache(), getTrackCache("2")))

        assertEquals(true,
            repository.fetchData(SortingState.ByTime()).first().map(TestTracksResultMapper(list))
        )
    }

    @Test
    fun `test check insert data`() = runBlocking {
        tracksDao.list.addAll(listOf(getTrackCache("1")))
        val result1 = repository.checkInsertData(getMediaItem("1"))

        assertEquals(TracksResult.Duplicate, result1)

        tracksDao.list.clear()

        val result2 = repository.checkInsertData(getMediaItem("1"))
        assertEquals(TracksResult.Success::class, result2::class)
    }


    @Test
    fun `test insert data`() = runBlocking {
        assertEquals(0,tracksDao.list.size)
        val result = repository.insertData(getMediaItem("1"))
        assertEquals(TracksResult.Success::class, result::class)
        assertEquals(1,tracksDao.list.size)
    }

    @Test
    fun `test contains`() = runBlocking {
        val result1 = repository.contains("1")
        assertEquals(false, result1)

        tracksDao.list.add(getTrackCache("1"))
        val result2 = repository.contains("1")
        assertEquals(true, result2)
    }

    @Test
    fun `test remove`() = runBlocking {
        tracksDao.list.add(getTrackCache("1"))
        val result = repository.removeTrack("1")
        assertEquals(TracksResult.Success::class, result::class)
        assertEquals(0,tracksDao.list.size)
    }

    class TestTracksResultMapper(
        private val expectedList: List<MediaItem> = emptyList(),
        private val expectedMessage: String =""
    ): TracksResult.Mapper<Boolean>{

        override suspend fun map(message: String, list: List<MediaItem>): Boolean {
            return message == expectedMessage
        }

        override suspend fun map(
            message: String,
            list: List<MediaItem>,
            albumDescription: String,
            albumName: String,
            albumImgUrl: String,
        ): Boolean {
            return message == expectedMessage
        }

    }

    class TestTracksDao: TracksDao{
        val list = emptyList<TrackCache>().toMutableList()

        override suspend fun insertTrack(track: TrackCache) {
           list.add(track)
        }

        override fun getTracksByTime(query: String): Flow<List<TrackCache>> = flow {
            emit(list.sortedBy { it.time })
        }

        override fun getTracksByName(query: String): Flow<List<TrackCache>> = flow {
            emit(list.sortedBy { it.name })
        }

        override fun getTracksByArtist(query: String): Flow<List<TrackCache>> = flow {
            emit(list.sortedBy { it.artistName })
        }

        override suspend fun contains(itemId: String): TrackCache? = list.find { it.id ==itemId }

        override suspend fun removeTrack(itemId: String) {
           list.removeIf { it.id == itemId }
        }

    }
}