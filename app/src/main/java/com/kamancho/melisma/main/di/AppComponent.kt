package com.kamancho.melisma.main.di

import android.content.Context
import com.kamancho.melisma.captcha.presentation.CaptchaFragmentDialog
import com.kamancho.melisma.creteplaylist.di.PlaylistDataComponent
import com.kamancho.melisma.downloader.presentation.DownloadTrackBroadcastReceiver
import com.kamancho.melisma.favorites.di.FavoriteComponent
import com.kamancho.melisma.frienddetails.di.FriendDetailsComponent
import com.kamancho.melisma.friends.di.FriendsComponent
import com.kamancho.melisma.main.presentation.MainActivity
import com.kamancho.melisma.musicdialog.presentation.AddTrackDialogFragment
import com.kamancho.melisma.notifications.presentation.NotificationsFragment
import com.kamancho.melisma.player.di.PlayerComponent
import com.kamancho.melisma.popular.di.PopularComponent
import com.kamancho.melisma.search.di.SearchComponent
import com.kamancho.melisma.searchhistory.di.SearchHistoryComponent
import com.kamancho.melisma.selectplaylist.di.SelectPlaylistComponent
import com.kamancho.melisma.settings.presentation.DeleteDownloadedTracksDialogFragment
import com.kamancho.melisma.settings.presentation.LogoutDialogFragment
import com.kamancho.melisma.settings.presentation.SettingsFragment
import com.kamancho.melisma.trending.di.TrendingComponent
import com.kamancho.melisma.userplaylists.di.PlaylistsComponent
import com.kamancho.melisma.vkauth.di.AuthComponent
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