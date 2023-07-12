package com.example.musicapp.main.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicapp.player.presentation.PlayerFragment
import com.example.musicapp.queue.presenatation.QueueFragment

/**
 * Created by HP on 22.04.2023.
 **/
class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val fragments = listOf(PlayerFragment(),QueueFragment())
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
