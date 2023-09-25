package com.kamancho.melisma.addtoplaylist.domain

import android.view.View
import com.kamancho.melisma.R
import com.kamancho.melisma.addtoplaylist.presentation.SelectedTrackUi
import com.kamancho.melisma.app.core.CacheRepository
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.creteplaylist.presentation.SelectedTracksStore
import com.kamancho.melisma.favorites.data.SortingState
import com.kamancho.melisma.main.di.AppModule.Companion.mainPlaylistId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by HP on 26.07.2023.
 **/
interface SelectedTracksInteractor {

    suspend fun isDbEmpty(): Boolean

     fun handleItem(item: SelectedTrackUi): List<SelectedTrackUi>

    suspend fun map(sortingState: SortingState, playlistId: String): List<SelectedTrackUi>

     fun saveList(list: List<SelectedTrackUi>)

    suspend fun selectedTracks(): List<SelectedTrackUi>

    fun resetOffset()

    class Base @Inject constructor(
        private val toDomainMapper: SelectedTrackUi.Mapper<SelectedTrackDomain>,
        private val repository: CacheRepository<SelectedTrackDomain>,
        private val store: SelectedTracksStore,
        private val managerResource: ManagerResource
    ): SelectedTracksInteractor {

        private val defaultToUiMapper =
            SelectedTrackDomain.ToUiSelected(View.GONE,managerResource.getColor(R.color.white))

        override suspend fun isDbEmpty(): Boolean = repository.isDbEmpty(mainPlaylistId.toString())

        override fun handleItem(item: SelectedTrackUi): List<SelectedTrackUi> {
            val selectedItems = store.read()
            val domainItem = item.map(toDomainMapper)
            val result = selectedItems.removeIf{ it.map(domainItem) }
            if(!result) selectedItems.push(domainItem)
            store.saveList(selectedItems)
            return selectedItems.map { it.map(defaultToUiMapper) }
        }


        override suspend fun map(sortingState: SortingState, playlistId: String): List<SelectedTrackUi> {
            val selectedItems = store.read()
            var selectedTrackBackgroundColor: Int
            var selectedTrackIconVisibility: Int
            return  repository.fetch(sortingState, playlistId).first().map { media ->
                val result = selectedItems.contains(media) // find { it.map(media) } != null
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

        override fun saveList(list: List<SelectedTrackUi>) = store.saveList(list.map { it.map(toDomainMapper) })
        override suspend fun selectedTracks(): List<SelectedTrackUi> = store.read().map { it.map(defaultToUiMapper) }
        override fun resetOffset() = repository.resetOffset()
    }
}