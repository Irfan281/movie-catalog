package com.irfan.moviecatalog.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.irfan.moviecatalog.ui.main.content.ContentFragment

class SectionPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = ContentFragment()
        fragment.arguments = Bundle().apply {
            putInt("number", position + 1)
        }
        return fragment
    }
}