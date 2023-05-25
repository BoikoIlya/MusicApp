package com.example.musicapp.updatesystem.data.cloud

import android.os.Parcelable
import com.example.musicapp.BuildConfig
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.updatesystem.data.UpdateResult
import kotlinx.parcelize.Parcelize

/**
 * Created by HP on 23.04.2023.
 **/
@Parcelize
data class UpdateDataCloud(
     val apkUrl: String,
     val description: String,
     val version: String,
) : Parcelable, Mapper<Unit, UpdateResult> {

    constructor(): this("", "", "")


    override fun map(data: Unit): UpdateResult {
        return if(version == BuildConfig.VERSION_NAME) UpdateResult.NoUpdate
        else {
            UpdateResult.NewUpdateInfo(version, description,apkUrl)
        }
    }
}
