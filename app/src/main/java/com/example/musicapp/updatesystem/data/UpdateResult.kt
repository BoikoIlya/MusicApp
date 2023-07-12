package com.example.musicapp.updatesystem.data

import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.updatesystem.presentation.UpdateDialogFragment
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/
sealed interface UpdateResult{

    suspend fun  <T>map(mapper: Mapper<T>):T

    interface Mapper<T>{
       suspend fun map(  version: String,
                  description: String,
                  apkUrl: String,
                  error: Int = -1): T
    }

    data class NewUpdateInfo(
        private val version: String,
        private val description: String,
        private val apkUrl: String
    ): UpdateResult {

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(version, description, apkUrl)
    }

    object NoUpdate: UpdateResult{

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map("","","")
    }




        data class Success(
            private val apkUrl: String
        ) : UpdateResult {
            override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map("", "", apkUrl)

        }

        data class Failure(
            private val error: Int =-1
        ) : UpdateResult {
            override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map("", "","", error)

        }


    }



    interface MainViewModelMapper : UpdateResult.Mapper<Unit> {


        @UnstableApi class Base @Inject constructor(
            private val updateDialogTransfer: DataTransfer.UpdateDialogTransfer,
            private val resourceManager: ManagerResource,
            private val singleUiEventCommunication: GlobalSingleUiEventCommunication
        ) : MainViewModelMapper {

            override suspend fun map(
                version: String,
                description: String,
                apkUrl: String,
                error: Int,
            ) {
                if (version.isNotEmpty() && description.isNotEmpty()) {
                    updateDialogTransfer.save(
                       Pair( resourceManager.getString(R.string.new_version) + version + "\n" + description,apkUrl)
                    )
                    singleUiEventCommunication.map(SingleUiEventState.ShowDialog(UpdateDialogFragment()))
                }
            }

        }
    }




