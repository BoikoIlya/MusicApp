package com.kamancho.melisma.frienddetails.presentation

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.frienddetails.domain.FriendIdAndNameTransfer
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
class FriendDetailsViewModel @Inject constructor(
    private val transfer: FriendIdAndNameTransfer,
    private val queryCommunication: SearchQueryFriendCommunication
): ViewModel() {

    fun readFirstAndSecondName(): String = transfer.read()!!.second

    fun saveQueryToCommunication(query: String) = queryCommunication.map(query)
}