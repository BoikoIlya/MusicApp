package com.example.musicapp.updatesystem.data

import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.UiEventState
import com.example.musicapp.main.presentation.UiEventsCommunication
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
                  apkUrl: String ="",
                  error: Int = -1): T
    }

    data class NewUpdateInfo(
        private val version: String,
        private val description: String
    ): UpdateResult {

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map(version, description)
    }

    object NoUpdate: UpdateResult{

        override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map("","")
    }




        data class Success(
            private val apkUrl: String
        ) : UpdateResult {
            override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map("", "", apkUrl)

        }

        data class Failure(
            private val error: Int
        ) : UpdateResult {
            override suspend fun <T> map(mapper: Mapper<T>): T = mapper.map("", "",error = error)

        }


    }



    interface MainViewModelMapper : UpdateResult.Mapper<Unit> {


        @UnstableApi class Base @Inject constructor(
            private val updateDialogTransfer: DataTransfer.UpdateDialogTransfer,
            private val resourceManager: ManagerResource,
            private val uiEventCommunication: UiEventsCommunication,
        ) : MainViewModelMapper {

            override suspend fun map(
                version: String,
                description: String,
                apkUrl: String,
                error: Int,
            ) {
                if (version.isNotEmpty() && description.isNotEmpty()) {
                    updateDialogTransfer.save(
                        resourceManager.getString(R.string.new_version) + version + "\n" + description
                    )
                    uiEventCommunication.map(UiEventState.ShowDialog(UpdateDialogFragment()))
                }
            }

        }
    }


    interface UpdateDialogMapper : UpdateResult.Mapper<Unit> {

        class Base @Inject constructor(
            private val singleUiEventCommunication: SingleUiEventCommunication,
            private val resourceManager: ManagerResource,
            private val uiEventCommunication: UiEventsCommunication,
        ) : UpdateDialogMapper {

            override suspend fun map(
                version: String,
                description: String,
                apkUrl: String,
                errorId: Int,
            ) {
                if (apkUrl.isNotEmpty()) {
                    uiEventCommunication.map(UiEventState.LoadUpdate(apkUrl))
                } else if (errorId != -1) {
                    singleUiEventCommunication.map(
                        SingleUiEventState.ShowSnackBar.Error(
                            resourceManager.getString(errorId)
                        )
                    )
                    uiEventCommunication.map(UiEventState.ShowDialog(UpdateDialogFragment()))
                }
            }

        }

    }

