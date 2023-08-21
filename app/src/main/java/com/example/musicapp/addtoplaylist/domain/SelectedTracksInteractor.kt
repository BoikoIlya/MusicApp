package com.example.musicapp.addtoplaylist.domain

import android.view.View
import com.example.musicapp.R
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.app.core.CacheRepository
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.creteplaylist.presentation.SelectedTracksStore
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.main.di.AppModule.Companion.mainPlaylistId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by HP on 26.07.2023.
 **/
interface SelectedTracksInteractor {

    suspend fun isDbEmpty(): Boolean

    suspend fun handleItem(item: SelectedTrackUi): List<SelectedTrackUi>

    suspend fun map(sortingState: SortingState, playlistId: String): Flow<List<SelectedTrackUi>>

    suspend fun saveList(list: List<SelectedTrackUi>)

    suspend fun selectedTracks(): List<SelectedTrackUi>

    class Base @Inject constructor(
        private val toDomainMapper: SelectedTrackUi.Mapper<SelectedTrackDomain>,
        private val repository: CacheRepository<SelectedTrackDomain>,
        private val store: SelectedTracksStore,
        private val managerResource: ManagerResource
    ): SelectedTracksInteractor {

        private val defaultToUiMapper =
            SelectedTrackDomain.ToUiSelected(View.GONE,managerResource.getColor(R.color.white))

        override suspend fun isDbEmpty(): Boolean = repository.isDbEmpty(mainPlaylistId.toString())

        override suspend fun handleItem(item: SelectedTrackUi): List<SelectedTrackUi> {
            val selectedItems = store.read()
            val domainItem = item.map(toDomainMapper)
            val result = selectedItems.removeIf{ it.map(domainItem) }
            if(!result) selectedItems.push(domainItem)
            store.saveList(selectedItems)
            return selectedItems.map { it.map(defaultToUiMapper) }
        }


        override suspend fun map(sortingState: SortingState, playlistId: String): Flow<List<SelectedTrackUi>> {
            val selectedItems = store.read()
            val tracks = repository.fetch(sortingState, playlistId)
            var selectedTrackBackgroundColor =  0
            var selectedTrackIconVisibility =  0
            return  tracks.map {list-> list.map { media ->
                val result = selectedItems.find { it.map(media) } != null
                if (result) {
                    selectedTrackBackgroundColor = managerResource.getColor(R.color.light_gray)
                    selectedTrackIconVisibility = View.VISIBLE
                } else {
                    selectedTrackBackgroundColor = managerResource.getColor(R.color.white)
                    selectedTrackIconVisibility = View.GONE
                }
                val mapper = SelectedTrackDomain.ToUiSelected(
                    selectedTrackIconVisibility,
                    selectedTrackBackgroundColor
                )
                media.map(mapper)
            }
            }
        }

        override suspend fun saveList(list: List<SelectedTrackUi>) = store.saveList(list.map { it.map(toDomainMapper) })
        override suspend fun selectedTracks(): List<SelectedTrackUi> = store.read().map { it.map(defaultToUiMapper) }
    }
}