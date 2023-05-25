package com.example.musicapp.main.di

import android.content.Context
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.favorites.di.FavoriteComponent
import com.example.musicapp.main.presentation.MainActivity
import com.example.musicapp.musicdialog.presentation.MusicDialogFragment
import com.example.musicapp.player.di.PlayerComponent
import com.example.musicapp.playlist.di.PlaylistComponent
import com.example.musicapp.queue.di.QueueComponent
import com.example.musicapp.search.di.SearchComponent
import com.example.musicapp.searchhistory.di.SearchHistoryComponent
import com.example.musicapp.trending.di.TrendingComponent
import com.example.musicapp.updatesystem.presentation.FCMUpdateService
import com.example.musicapp.updatesystem.presentation.UpdateDialogFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@UnstableApi /**
 * Created by HP on 29.01.2023.
 **/
@Singleton
@Component(modules = [AppModule::class, AppBindModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(dialogFragment: MusicDialogFragment)
    fun inject(dialogFragment: UpdateDialogFragment)

    fun trendingComponent(): TrendingComponent.Builder

    fun playerComponent(): PlayerComponent.Builder

    fun favoriteComponent(): FavoriteComponent.Builder

    fun queueComponent(): QueueComponent.Builder

    fun searchComponent(): SearchComponent.Builder

    fun searchHistoryComponent(): SearchHistoryComponent.Builder

    fun playlistComponent(): PlaylistComponent.Builder

}