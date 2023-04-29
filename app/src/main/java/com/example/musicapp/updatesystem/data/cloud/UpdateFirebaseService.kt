package com.example.musicapp.updatesystem.data.cloud

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleOwner
import com.example.musicapp.app.core.SingleUiEventState
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.io.File
import java.lang.Exception
import javax.inject.Inject


/**
 * Created by HP on 23.04.2023.
 **/
interface UpdateFirebaseService {

    suspend fun checkForUpdate(): UpdateDataCloud?

    class Base @Inject constructor(
        private val cloudDB: FirebaseFirestore,
    ):UpdateFirebaseService{

        companion object{
            private const val update_collection = "update"
            private const val update_doc_id = "m2AX77e9DIVv8e12l40A"
        }




        override suspend fun checkForUpdate(): UpdateDataCloud? {
                val cloudData =
                    cloudDB.collection(update_collection)
                        .document(update_doc_id)
                        .get().await()
               return cloudData.toObject(UpdateDataCloud::class.java)
        }



    }
}