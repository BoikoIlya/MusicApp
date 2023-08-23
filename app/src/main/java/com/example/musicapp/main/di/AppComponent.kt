package com.example.musicapp.main.di

import android.content.Context
import com.example.musicapp.captcha.presentation.CaptchaFragmentDialog
import com.example.musicapp.creteplaylist.di.PlaylistDataComponent
import com.example.musicapp.downloader.presentation.DownloadTrackBroadcastReceiver
import com.example.musicapp.favorites.di.FavoriteComponent
import com.example.musicapp.frienddetails.di.FriendDetailsComponent
import com.example.musicapp.friends.di.FriendsComponent
import com.example.musicapp.main.presentation.MainActivity
import com.example.musicapp.musicdialog.presentation.AddTrackDialogFragment
import com.example.musicapp.notifications.presentation.NotificationsFragment
import com.example.musicapp.player.di.PlayerComponent
import com.example.musicapp.popular.di.PopularComponent
import com.example.musicapp.search.di.SearchComponent
import com.example.musicapp.searchhistory.di.SearchHistoryComponent
import com.example.musicapp.selectplaylist.di.SelectPlaylistComponent
import com.example.musicapp.settings.presentation.DeleteDownloadedTracksDialogFragment
import com.example.musicapp.settings.presentation.LogoutDialogFragment
import com.example.musicapp.settings.presentation.SettingsFragment
import com.example.musicapp.trending.di.TrendingComponent
import com.example.musicapp.userplaylists.di.PlaylistsComponent
import com.example.musicapp.vkauth.di.AuthComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
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
    fun inject(dialogFragment: AddTrackDialogFragment)
    fun inject(captchaFragmentDialog: CaptchaFragmentDialog)
    fun inject(notificationsFragment: NotificationsFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(downloadTrackBroadcastReceiver: DownloadTrackBroadcastReceiver)
    fun inject(logoutDialogFragment: LogoutDialogFragment)
    fun inject(deleteDownloadedTracksDialogFragment: DeleteDownloadedTracksDialogFragment)

    fun trendingComponent(): TrendingComponent.Builder

    fun playerComponent(): PlayerComponent.Builder

    fun favoriteComponent(): FavoriteComponent.Builder

    fun searchComponent(): SearchComponent.Builder

    fun searchHistoryComponent(): SearchHistoryComponent.Builder

    fun authComponent(): AuthComponent.Builder

    fun playlistsComponent(): PlaylistsComponent.Builder

    fun playlistDataComponent(): PlaylistDataComponent.Builder

    fun selectPlaylistComponent(): SelectPlaylistComponent.Builder

    fun friendsComponent(): FriendsComponent.Builder

    fun friendDetailsComponent(): FriendDetailsComponent.Builder

    fun popularComponent(): PopularComponent.Builder


}