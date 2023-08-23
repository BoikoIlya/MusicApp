package com.kamancho.melisma.frienddetails.domain

import com.kamancho.melisma.app.core.DataTransfer
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface FriendIdAndNameTransfer: DataTransfer<Pair<String,String>> {

    class Base @Inject constructor(): FriendIdAndNameTransfer, DataTransfer.Abstract<Pair<String,String>>()
}