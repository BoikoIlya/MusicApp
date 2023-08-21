package com.example.musicapp.hlscachesystem.data.cache

import android.util.Log
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.cache.CacheKeyFactory
import com.example.musicapp.app.core.DataTransfer
import javax.inject.Inject

/**
 * Created by HP on 10.08.2023.
 **/
class BaseCacheKeyFactory @Inject constructor(
    private val transfer: DataTransfer<String>
): CacheKeyFactory {

    override fun buildCacheKey(dataSpec: DataSpec): String {
        Log.d("tag", "buildCacheKey: ${transfer.read()!!} ")
        return dataSpec.key ?: dataSpec.uri.toString() //transfer.read()!!
    }
}