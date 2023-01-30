package com.example.musicapp.app.app.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

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