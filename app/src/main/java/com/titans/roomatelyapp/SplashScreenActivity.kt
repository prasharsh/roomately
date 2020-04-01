package com.titans.roomatelyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashScreenActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        //Creating a thread object
        val background = object : Thread()
        {
            override fun run()
            {
                super.run()
                try
                {
                    // MainActivity will start after 5 seconds
                    Thread.sleep(3000)
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)

                } catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}
