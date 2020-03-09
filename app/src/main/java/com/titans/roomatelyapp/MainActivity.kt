package com.titans.roomatelyapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.titans.roomatelyapp.MainWindowTabs.Dashboard
import com.titans.roomatelyapp.MainWindowTabs.Groups
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tab_dashboard.*

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sectionPageAdapter=SectionPageAdapter(supportFragmentManager)
        sectionPageAdapter.addPage(Dashboard(),"Dashboard")
        sectionPageAdapter.addPage(Groups(),"Groups")

        view_pager.adapter=sectionPageAdapter
        tabs.setupWithViewPager(view_pager)
    }
}
