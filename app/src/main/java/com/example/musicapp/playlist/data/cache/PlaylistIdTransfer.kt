package com.example.musicapp.playlist.data.cache

import com.example.musicapp.app.core.DataTransfer
import javax.inject.Inject

/**
 * Created by HP on 04.05.2023.
 **/
interface PlaylistIdTransfer: DataTransfer<String>  {

    class Base @Inject constructor(): PlaylistIdTransfer,DataTransfer.Abstract<String>()
}