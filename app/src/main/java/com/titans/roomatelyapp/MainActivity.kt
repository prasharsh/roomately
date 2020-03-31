package com.titans.roomatelyapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.titans.roomatelyapp.DataModels.Invitation
import com.titans.roomatelyapp.MainWindowTabs.Dashboard
import com.titans.roomatelyapp.MainWindowTabs.Groups
import com.titans.roomatelyapp.DataModels.User
import com.titans.roomatelyapp.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.tab_groups.*

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        auth = FirebaseAuth.getInstance()

        Log.e("TAG","started")
        Data.init()
        checkLogin()

        dehaze.setOnClickListener { v ->
            if(drawer.isDrawerOpen(navigationView))
                drawer.closeDrawer(navigationView)
            else
                drawer.openDrawer(navigationView)
        }

        var sectionPageAdapter=SectionPageAdapter(supportFragmentManager)
        sectionPageAdapter.addPage(Dashboard(),"Dashboard")
        sectionPageAdapter.addPage(Groups(),"Groups")

        view_pager.adapter=sectionPageAdapter
        tabs.setupWithViewPager(view_pager)

        view_pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int)
            {
                if(position==1)
                {
                    getInvitations()
                    Log.e("TAG","Group Tab")
                }
            }

        })

        navigationView.setNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.edit_profile -> {
                    Toast.makeText(this,"Edit Profile",Toast.LENGTH_LONG).show()
                    Log.e("TAG","EDIT")
                    return@setNavigationItemSelectedListener true
                }
                R.id.delete_account -> {
                    Toast.makeText(this, "Delete Account", Toast.LENGTH_LONG).show()
                    return@setNavigationItemSelectedListener true
                }
                else -> return@setNavigationItemSelectedListener false
            }
        }

        txtLogout.setOnClickListener { v ->
            var editor = getSharedPreferences(Data.SHAREDPREF, Context.MODE_PRIVATE).edit()
            editor.putString(Data.SAVEDUSER,"")

            editor.commit()

            var i = Intent(this,LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
    }

    fun checkLogin()
    {

        var sharedPref = getSharedPreferences(Data.SHAREDPREF,Context.MODE_PRIVATE)

        var user = sharedPref.getString(Data.SAVEDUSER,"")

        if(user.equals(""))
        {
            var i = Intent(this,LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
        else
        {
            Log.e("TAG","Phone:"+user)
            Data.db.collection(Data.USERS).document(user!!).get()
                .addOnSuccessListener { documentSnapshot ->

                    Log.e("TAG","Phone From FIrestore: "+documentSnapshot.getString(Data.USER_PHONE))
                    Data.currentUser = User(
                        name = documentSnapshot.getString(Data.USER_NAME)!!,
                        phone = documentSnapshot.getString(Data.USER_PHONE)!!,
                        groups = documentSnapshot.get(Data.USER_GROUPS) as ArrayList<String>
                    )

                    getInvitations()
                    Data.setTranscationListners(applicationContext)
                    setNavigationHeader()
                }
        }
    }

    fun setNavigationHeader()
    {
        var txtName = navigationView.getHeaderView(0).findViewById<TextView>(R.id.txtName)
        var txtPhone = navigationView.getHeaderView(0).findViewById<TextView>(R.id.txtPhone)
        var spinGroups = navigationView.getHeaderView(0).findViewById<Spinner>(R.id.spinGroups)

        txtName.text = Data.currentUser.name
        txtPhone.text = Data.currentUser.phone

        if(Data.groups.size==0)
            Data.createGroupList()

        var adapter = ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,Data.groups)
        adapter.setNotifyOnChange(true)
        spinGroups.adapter = adapter



        spinGroups.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                Toast.makeText(applicationContext,Data.groups[position],Toast.LENGTH_LONG).show()
                Data.selectedGroup = Data.groups[position]
            }
        }

        if(Data.groupListAdapter!=null)
            Data.groupListAdapter!!.notifyDataSetChanged()
    }

    fun getInvitations()
    {
        Data.db.collection(Data.USERS).document(Data.currentUser.phone).get()
            .addOnSuccessListener { documentSnapshot ->
                var list = documentSnapshot["invitations"]

                if(list!=null) {
                    Data.invitations.clear()
                    for (invitation in (list as ArrayList<HashMap<String,String>>))
                    {
                        Data.invitations.add(Invitation(group = invitation["group"]!!,phone = invitation["phone"]!!))
                    }
                        Data.txtInvitations.value = list.size
                }
            }
            .addOnFailureListener{exception ->
                Toast.makeText(this,"Error Getting Invitations",Toast.LENGTH_LONG).show()
            }
    }

}
