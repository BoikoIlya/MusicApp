package com.example.musicapp.frienddetails.domain

import com.example.musicapp.app.core.DataTransfer
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface FriendIdAndNameTransfer: DataTransfer<Pair<String,String>> {

    class Base @Inject constructor(): FriendIdAndNameTransfer, DataTransfer.Abstract<Pair<String,String>>()
}