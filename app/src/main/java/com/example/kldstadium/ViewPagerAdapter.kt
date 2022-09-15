package com.example.kldstadium

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter (supportFragmentManager: FragmentManager):
    FragmentStatePagerAdapter(supportFragmentManager){
    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitleList = ArrayList<String>()


    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String){
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

}