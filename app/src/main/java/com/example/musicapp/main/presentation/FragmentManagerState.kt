package com.example.musicapp.main.presentation

import android.transition.Slide
import android.view.Gravity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.musicapp.R

/**
 * Created by HP on 20.06.2023.
 **/
sealed interface FragmentManagerState{

    fun apply(
        fragmentManager: FragmentManager
    )

    object Empty: FragmentManagerState{
        override fun apply(fragmentManager: FragmentManager) = Unit
    }

    abstract class Replace(): FragmentManagerState{

       protected abstract fun fragment(): Fragment

        override fun apply(fragmentManager: FragmentManager) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment()).commit()
        }


        class WithAnimation(
            private val fragment: Fragment
        ): Replace(){

            override fun fragment(): Fragment = fragment.apply {
                enterTransition = Slide(Gravity.END)
                exitTransition = Slide(Gravity.START)
            }

        }

        class WithoutAnimation(
            private val fragment: Fragment
        ): Replace(){

            override fun fragment(): Fragment = fragment


        }

    }

}