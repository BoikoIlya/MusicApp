package com.kamancho.melisma.frienddetails.presentation

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.FlowCollector

/**
 * Created by HP on 18.08.2023.
 **/
interface FriendsDataViewModel {

    //todo
    fun search(query: String){}

    fun search(query: String,id: String){}

    suspend fun collectSearchQuery(
        owner: LifecycleOwner,
        collector: FlowCollector<String>)
}