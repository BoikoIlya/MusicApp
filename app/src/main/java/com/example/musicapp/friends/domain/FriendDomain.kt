package com.example.musicapp.friends.domain

import androidx.room.PrimaryKey
import com.example.musicapp.friends.presentation.FriendUi

/**
 * Created by HP on 17.08.2023.
 **/
data class FriendDomain(
   private val id: Int,
   private val firstName: String,
   private val secondName: String,
   private val photoUrl: String
){

    fun map() = FriendUi(id = id, firstAndSecondName = String.format("$firstName $secondName") , photoUrl = photoUrl)
}