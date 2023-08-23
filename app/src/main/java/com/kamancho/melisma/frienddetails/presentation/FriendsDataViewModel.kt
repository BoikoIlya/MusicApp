package com.kamancho.melisma.frienddetails.presentation

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.FlowCollector

/**
 * Created by HP on 18.08.2023.
 **/
interface FriendsDataViewModel {

    fun search(query: String)

    suspend fun collectSearchQuery(
        owner: LifecycleOwner,
        collector: FlowCollector<String>)
}