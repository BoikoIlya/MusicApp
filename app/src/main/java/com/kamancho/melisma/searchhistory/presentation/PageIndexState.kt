package com.kamancho.melisma.searchhistory.presentation

import androidx.viewpager2.widget.ViewPager2

/**
 * Created by HP on 15.08.2023.
 **/
sealed interface PageIndexState{

    fun apply(viewPager: ViewPager2)

    object Empty: PageIndexState{
        override fun apply(viewPage: ViewPager2) = Unit
    }

    data class SetIndex(
        private val index: Int
    ): PageIndexState{

        override fun apply(viewPager: ViewPager2) {
            viewPager.setCurrentItem(index,false)
        }

    }
}