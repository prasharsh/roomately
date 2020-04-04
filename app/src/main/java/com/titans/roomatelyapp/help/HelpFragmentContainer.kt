package com.titans.roomatelyapp.help

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.titans.roomatelyapp.R
import kotlinx.android.synthetic.main.activity_help_fragment_container.*

class HelpFragmentContainer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_fragment_container)

        if(savedInstanceState!=null)
            supportFragmentManager.beginTransaction().add(R.id.helpFragment,HelpButtonsFragment())
    }
}
