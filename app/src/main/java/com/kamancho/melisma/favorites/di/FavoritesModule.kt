package com.kamancho.melisma.favorites.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.CacheRepository
import com.kamancho.melisma.app.core.HandleFavoritesTracksSortedSearch
import com.kamancho.melisma.app.core.PagingSource
import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.favorites.presentation.*
import com.kamancho.melisma.main.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * Created by HP on 21.03.2023.
 **/
@Module
interface FavoritesModule {

    @Binds
    @FavoritesScope
    fun bindCachedTracksRepositoryBaseMediaItem(obj: CacheRepository.BaseMediaItem): CacheRepository<MediaItem>



    @FavoritesScope
    @Binds
    fun bindHandlerFavoritesTracksUiUpdate(obj: HandlerFavoritesTracksUiUpdate.Base): HandlerFavoritesTracksUiUpdate

    @Binds
    @FavoritesScope
    fun bindHandleFavoritesTracksFromCache(obj: HandleFavoritesTracksFromCache.Base):HandleFavoritesTracksFromCache

    @Binds
    @FavoritesScope
    fun bindHandleFavoritesTracksSortedSearch(obj: HandleFavoritesTracksSortedSearch.Base):HandleFavoritesTracksSortedSearch


    @FavoritesScope
    @Binds
    fun bindFavoritesStateCommunication(obj: FavoritesStateCommunication.Base): FavoritesStateCommunication

    @FavoritesScope
    @Binds
    fun bindFavoritesTrackListCommunication(obj: FavoritesTrackListCommunication.Base): FavoritesTrackListCommunication

    @FavoritesScope
    @Binds
    fun bindTracksResultToFavoriteTracksCommunicationMapper(obj: TracksResultToFavoriteTracksCommunicationMapper.Base): TracksResultToFavoriteTracksCommunicationMapper

    @FavoritesScope
    @Binds
    fun bindFavoritesTracksLoadingCommunication(obj: FavoritesTracksLoadingCommunication.Base): FavoritesTracksLoadingCommunication



    @Binds
    @[IntoMap ViewModelKey(FavoritesTracksViewModel::class)]
    fun bindFavoritesViewModel(favoritesViewModel: FavoritesTracksViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(FavoritesBottomSheetMenuViewModel::class)]
    fun bindFavoritesBottomSheetMenuViewModel(favoritesBottomSheetMenuViewModel: FavoritesBottomSheetMenuViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(DeleteDialogViewModel::class)]
    fun bindDeleteDialogViewModel(deleteDialogViewModel: DeleteDialogViewModel): ViewModel
}

@Module
class FavoritesProvidesModule{

    @Provides
    @FavoritesScope
    fun provideFavoritesTracksCommunication(
        uiStateCommunication: FavoritesStateCommunication,
        favoritesTrackListCommunication: FavoritesTrackListCommunication,
        favoritesTracksLoadingCommunication: FavoritesTracksLoadingCommunication
    ): FavoritesTracksCommunication{
        return FavoritesTracksCommunication.Base(uiStateCommunication,favoritesTrackListCommunication,favoritesTracksLoadingCommunication)
    }
}