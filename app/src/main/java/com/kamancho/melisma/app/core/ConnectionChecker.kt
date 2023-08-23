package com.kamancho.melisma.app.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

/**
 * Created by HP on 04.07.2023.
 **/
interface ConnectionChecker {

    suspend fun isDeviceHaveConnection(): Boolean

    class Base @Inject constructor(
        private val context: Context
    ) : ConnectionChecker{

        override suspend fun isDeviceHaveConnection(): Boolean {

            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return manager.getNetworkCapabilities(manager.activeNetwork)?.let {
                        it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            } ?: false
        }
    }
}