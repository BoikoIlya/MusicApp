package com.example.musicapp.updatesystem.data.cloud

import android.os.Parcelable
import com.example.musicapp.BuildConfig
import com.example.musicapp.updatesystem.data.UpdateResult
import com.example.musicapp.updatesystem.data.cache.UpdateDataStore
import kotlinx.parcelize.Parcelize

/**
 * Created by HP on 23.04.2023.
 **/
@Parcelize
data class UpdateDataCloud(
     val apkUrl: String,
     val description: String,
     val version: String,
) : Parcelable {

    constructor(): this("", "", "")

    fun map(
         versionStore: UpdateDataStore.Version,
         apkUrlStore: UpdateDataStore.ApkUrl,
         descriptionStore: UpdateDataStore.Description,
    ): UpdateResult{
        return if(version == BuildConfig.VERSION_NAME) UpdateResult.NoUpdate
        else {
            versionStore.save(version)
            apkUrlStore.save(apkUrl)
            descriptionStore.save(description)
            UpdateResult.NewUpdateInfo(version, description)
        }
    }
}
