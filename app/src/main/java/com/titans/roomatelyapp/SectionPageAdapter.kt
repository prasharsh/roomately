package com.titans.roomatelyapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionPageAdapter: FragmentPagerAdapter
{
    constructor(fm: FragmentManager) : super(fm)

    var pages = ArrayList<Fragment>()
    var pageTitles = ArrayList<String>()

    fun addPage(page:Fragment,title:String)
    {
        pages.add(page)
        pageTitles.add(title)
    }

    override fun getItem(position: Int): Fragment
    {
        return pages[position]
    }

    override fun getCount(): Int
    {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence?
    {
        return pageTitles[position]
    }
}