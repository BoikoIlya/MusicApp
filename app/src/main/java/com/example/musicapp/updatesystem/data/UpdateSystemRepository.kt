package com.example.musicapp.updatesystem.data

import com.example.musicapp.BuildConfig
import com.example.musicapp.R
import com.example.musicapp.updatesystem.data.cache.UpdateDataStore
import com.example.musicapp.updatesystem.data.cloud.UpdateFirebaseService
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/
interface UpdateSystemRepository {

    suspend fun checkForNewVersion(): UpdateResult

    suspend fun retriveApkUrl():UpdateResult

    class Base @Inject constructor(
       private val versionStore: UpdateDataStore.Version,
       private val apkUrlStore: UpdateDataStore.ApkUrl,
       private val descriptionStore: UpdateDataStore.Description,
       private val cloud: UpdateFirebaseService.Base
    ): UpdateSystemRepository{


        override suspend fun checkForNewVersion(): UpdateResult {
            val cachedVersion = versionStore.read()

            return if(cachedVersion != BuildConfig.VERSION_NAME)
                UpdateResult.NewUpdateInfo(cachedVersion, descriptionStore.read())
            else {
                try {
                    val newVersion = cloud.checkForUpdate()
                    newVersion?.map(versionStore, apkUrlStore, descriptionStore)
                        ?: UpdateResult.Failure(-1)
                }catch (e: Exception){
                        UpdateResult.Failure(-1)
                }
            }
        }

        override suspend fun retriveApkUrl(): UpdateResult {
            val url = apkUrlStore.read()
            return if(url.isEmpty()) UpdateResult.Failure(R.string.empty_url_error)
            else UpdateResult.Success(url)

        }


    }
}