package com.example.musicapp.updatesystem.data


import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.updatesystem.data.cloud.UpdateFirebaseService
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/
interface UpdateSystemRepository {

    suspend fun checkForNewVersion(): UpdateResult

    class Base @Inject constructor(
       private val cloud: UpdateFirebaseService.Base,
    ): UpdateSystemRepository{


        override suspend fun checkForNewVersion(): UpdateResult {
            return try {
                    val newVersion = cloud.checkForUpdate()
                    newVersion?.map(Unit) ?: UpdateResult.Failure()
                }catch (e: Exception){
                        UpdateResult.Failure()
                }
            }


    }
}