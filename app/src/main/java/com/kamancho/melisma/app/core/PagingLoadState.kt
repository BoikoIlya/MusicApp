package com.kamancho.melisma.app.core

/**
 * Created by Ilya Boiko @camancho
on 26.10.2023.
 **/
sealed interface PagingLoadState {

    fun apply(loadStateAdapter: LoadStateAdapter, pagingLister: PagingListener)

    object Loading : PagingLoadState {
        override fun apply(loadStateAdapter: LoadStateAdapter, pagingLister: PagingListener) {
            loadStateAdapter.showState(listOf(""))
            pagingLister.setIsLoading(true)
        }
    }

    object Hide : PagingLoadState {
        override fun apply(loadStateAdapter: LoadStateAdapter, pagingLister: PagingListener) {
            loadStateAdapter.showState(emptyList())
            pagingLister.setIsLoading(false)
        }
    }

    data class Failure(
     private val message: String,
    ) : PagingLoadState {
        override fun apply(loadStateAdapter: LoadStateAdapter, pagingLister: PagingListener) {
            loadStateAdapter.showState(listOf(message))
        }
    }

    object Empty : PagingLoadState {
        override fun apply(loadStateAdapter: LoadStateAdapter, pagingLister: PagingListener) = Unit
    }

}