package com.titans.roomatelyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.titans.roomatelyapp.MainWindowTabs.Dashboard
import com.titans.roomatelyapp.MainWindowTabs.Groups
import com.titans.roomatelyapp.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        var sectionPageAdapter=SectionPageAdapter(supportFragmentManager)
        sectionPageAdapter.addPage(Dashboard(),"Dashboard")
        sectionPageAdapter.addPage(Groups(),"Groups")

        view_pager.adapter=sectionPageAdapter
        tabs.setupWithViewPager(view_pager)
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}
