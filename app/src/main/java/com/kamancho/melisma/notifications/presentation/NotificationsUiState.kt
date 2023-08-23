package com.kamancho.melisma.notifications.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.simform.refresh.SSPullToRefreshLayout

/**
 * Created by HP on 21.08.2023.
 **/
sealed interface NotificationsUiState{

    fun apply(
        message: TextView,
        notificationImg: ImageView,
        pullToRefreshLayout: SSPullToRefreshLayout,
        rcv: RecyclerView
        )

    object Loading: NotificationsUiState {
        override fun apply(
            message: TextView,
            notificationImg: ImageView,
            pullToRefreshLayout: SSPullToRefreshLayout,
            rcv: RecyclerView
        ) {
            rcv.visibility = View.VISIBLE
            message.visibility = View.GONE
            notificationImg.visibility = View.GONE
            pullToRefreshLayout.setRefreshing(true)
        }
    }

    open class DisableLoading: NotificationsUiState {
        override fun apply(
            message: TextView,
            notificationImg: ImageView,
            pullToRefreshLayout: SSPullToRefreshLayout,
            rcv: RecyclerView
        ) {
            pullToRefreshLayout.setRefreshing(false)
            pullToRefreshLayout.isEnabled = false
            pullToRefreshLayout.isEnabled = true
        }
    }

    object EmptyList: DisableLoading(){
        override fun apply(
            message: TextView,
            notificationImg: ImageView,
            pullToRefreshLayout: SSPullToRefreshLayout,
            rcv: RecyclerView
        ) {
            super.apply(message, notificationImg, pullToRefreshLayout,rcv)
            rcv.visibility = View.GONE
            message.visibility = View.VISIBLE
            notificationImg.visibility = View.VISIBLE
        }
    }
}