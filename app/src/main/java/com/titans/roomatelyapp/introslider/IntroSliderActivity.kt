package com.titans.roomatelyapp.introslider

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.titans.roomatelyapp.R
import com.titans.roomatelyapp.SplashScreenActivity
import com.titans.roomatelyapp.login.LoginActivity
import kotlinx.android.synthetic.main.activity_slider.*
import kotlinx.android.synthetic.main.dialog_change_password.*

class IntroSliderActivity : AppCompatActivity(){

    val fragmentPage1 = SliderStartFragment()
    val fragmentPage2 = SliderStartFragment()
    val fragmentPage3 = SliderStartFragment()
    lateinit var pagerAdapter: introPagerAdapter
    lateinit var activity: Activity
    lateinit var preferences: SharedPreferences
    val pref_show_intro ="Intro"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)
        activity  =this
        preferences = getSharedPreferences("IntroSlider", Context.MODE_PRIVATE)
        if(!preferences.getBoolean(pref_show_intro,true)){
            startActivity(Intent(activity,SplashScreenActivity::class.java))
            finish()
        }
        fragmentPage1.setIntroText("Welcome to Roomately. Make your life with roommates easier.")
        fragmentPage2.setIntroText("Manage your inventory so that you never run out of your groceries")
        fragmentPage3.setIntroText("Keep track of your shared expenses and split them")
        fragmentPage1.setIntroImage(R.drawable.ic_mobile)
        fragmentPage2.setIntroImage(R.drawable.ic_shopping)
        fragmentPage3.setIntroImage(R.drawable.ic_wallet)

        pagerAdapter = introPagerAdapter(supportFragmentManager)
        pagerAdapter.fragment_list.add(fragmentPage1)
        pagerAdapter.fragment_list.add(fragmentPage2)
        pagerAdapter.fragment_list.add(fragmentPage3)

        introViewPager.adapter=pagerAdapter
        buttonNext.setOnClickListener{
            introViewPager.currentItem++
        }
        introViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == pagerAdapter.fragment_list.size-1){
                    buttonNext.text="DONE"
                    buttonNext.setOnClickListener(){
                        startActivity(Intent(activity,LoginActivity::class.java))
                        finish()
                        val editor = preferences.edit()
                        editor.putBoolean(pref_show_intro, false)
                    }
                }
                else{
                    buttonNext.text=="NEXT"
                    buttonNext.setOnClickListener{
                        introViewPager.currentItem++
                    }
                }
                when(introViewPager.currentItem){
                    0->{
                        swipe1.setTextColor(Color.BLACK)
                        swipe2.setTextColor(Color.GRAY)
                        swipe3.setTextColor(Color.GRAY)
                    }
                    1->{
                        swipe1.setTextColor(Color.GRAY)
                        swipe2.setTextColor(Color.BLACK)
                        swipe3.setTextColor(Color.GRAY)
                    }
                    2->{
                        swipe1.setTextColor(Color.GRAY)
                        swipe2.setTextColor(Color.GRAY)
                        swipe3.setTextColor(Color.BLACK)
                    }
                }
            }

        })

    }

    class introPagerAdapter(manager : FragmentManager) : FragmentPagerAdapter(manager){

        val fragment_list : MutableList<Fragment> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return fragment_list[position]
        }

        override fun getCount(): Int {
            return fragment_list.size
        }

    }





    }