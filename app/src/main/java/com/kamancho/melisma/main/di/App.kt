package com.kamancho.melisma.main.di

import android.app.Application
import com.kamancho.melisma.BuildConfig
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

/**
 * Created by HP on 28.01.2023.
 **/
class App: Application() {

    companion object{
        private const val API_KEY:String = "bf9e4443-af4b-4546-ae44-46bf0f0b61b0"
    }

    lateinit var appComponent: AppComponent


    override fun onCreate() {
        appComponent = DaggerAppComponent.builder().context(this).build()
        super.onCreate()

        if(BuildConfig.DEBUG) return
        val config = AppMetricaConfig.newConfigBuilder(API_KEY).build()
        AppMetrica.activate(this, config)
    }
}