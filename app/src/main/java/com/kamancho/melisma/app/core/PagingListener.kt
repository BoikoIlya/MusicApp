package com.kamancho.melisma.app.core

import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Ilya Boiko @camancho
on 26.10.2023.
 **/

abstract class PagingListener : RecyclerView.OnScrollListener() {

    protected var isLoading = false
    protected var isScrolling = false


    fun setIsLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }


    class Base(
        private val pageSize: Int,
        private val triggerCount: Int,
        private val loadPage: () -> Unit,
    ) : PagingListener() {


        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)


            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoading = !isLoading
            val isAtLastItem =
                firstVisibleItemPosition + visibleItemCount >= (totalItemCount - triggerCount)
            val isNotAtTheBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= pageSize

            val shouldPaginate = isNotLoading && isAtLastItem && isNotAtTheBeginning
                    && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                loadPage.invoke()
                isScrolling = false
            }


        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            isScrolling = newState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                val isNotLoadingAndNotLastPage = !isLoading
                val isAtLastItem =
                    firstVisibleItemPosition + visibleItemCount >= (totalItemCount - triggerCount)
                val isNotAtTheBeginning = firstVisibleItemPosition >= 0
                val isTotalMoreThanVisible = totalItemCount >= pageSize

                val shouldPaginate =
                    isNotLoadingAndNotLastPage && isAtLastItem && isNotAtTheBeginning
                            && isTotalMoreThanVisible

                if (shouldPaginate) {
                    loadPage.invoke()
                    isScrolling = false
                }
            }
        }


    }
}