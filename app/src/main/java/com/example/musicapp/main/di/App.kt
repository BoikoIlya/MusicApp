package com.example.musicapp.main.di

import android.app.Application
import com.example.musicapp.favorites.di.FavoriteComponent
import dagger.internal.DaggerCollections

/**
 * Created by HP on 28.01.2023.
 **/
class App: Application() {


    lateinit var appComponent: AppComponent


    override fun onCreate() {
        appComponent = DaggerAppComponent.builder().context(this).build()
        super.onCreate()
    }
}