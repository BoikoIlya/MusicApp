package com.example.musicapp.main.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicapp.player.presentation.PlayerFragment
import com.example.musicapp.queue.presenatation.QueueFragment

/**
 * Created by HP on 22.04.2023.
 **/
class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment{
        return when(position){
            0-> PlayerFragment()
            1-> QueueFragment()
            else -> PlayerFragment()
        }
    }
}