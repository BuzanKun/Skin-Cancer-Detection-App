package com.dicoding.asclepius.view

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.asclepius.view.analyze.AnalyzeFragment
import com.dicoding.asclepius.view.history.HistoryFragment
import com.dicoding.asclepius.view.news.NewsFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = NewsFragment()
            1 -> fragment = AnalyzeFragment()
            2 -> fragment = HistoryFragment()
        }
        return fragment as Fragment
    }
}