package com.titans.roomatelyapp

import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.firebase.firestore.SetOptions
import com.titans.roomatelyapp.DataModels.Invitation
import com.titans.roomatelyapp.MainWindowTabs.Dashboard
import com.titans.roomatelyapp.MainWindowTabs.Groups
import com.titans.roomatelyapp.DataModels.User
import com.titans.roomatelyapp.dialogs.ChangePasswordDialog
import com.titans.roomatelyapp.dialogs.EditNameDialog
import com.titans.roomatelyapp.help.HelpFragmentContainer
import com.titans.roomatelyapp.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.tab_groups.*

class MainActivity : AppCompatActivity()
{
    lateinit var txtName: TextView
    lateinit var txtPhone: TextView
    lateinit var spinGroups: Spinner
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        auth = FirebaseAuth.getInstance()

        Log.e("TAG","started")
        Data.init()
        checkLogin()

        txtName = navigationView.getHeaderView(0).findViewById<TextView>(R.id.txtName)
        txtPhone = navigationView.getHeaderView(0).findViewById<TextView>(R.id.txtPhone)
        spinGroups = navigationView.getHeaderView(0).findViewById<Spinner>(R.id.spinGroups)

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
                    Data.getInvitations(this@MainActivity)
                    Data.updateGroups()
                    Log.e("TAG","Group Tab")
                }
            }

        })

        navigationView.setNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.edit_name -> {
                    EditNameDialog(ctx = applicationContext, txtView = txtName).show(supportFragmentManager,"Edit Name")
                    Log.e("TAG","EDIT")
                    return@setNavigationItemSelectedListener true
                }
                R.id.change_password -> {
                    ChangePasswordDialog(ctx = this).show(supportFragmentManager, "Change Password")
                    Log.e("TAG","Change Pass")
                    return@setNavigationItemSelectedListener true
                }
                R.id.delete_account ->
                {
                    confirmAndDelete()
                    return@setNavigationItemSelectedListener true
                }
                R.id.help ->
                {
                    startActivity(Intent(this@MainActivity,HelpFragmentContainer::class.java))
                    return@setNavigationItemSelectedListener true
                }
                else -> return@setNavigationItemSelectedListener false
            }
        }

        txtLogout.setOnClickListener { v ->
            logOut()
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
                        pass = documentSnapshot.getString(Data.USER_PASS)!!,
                        groups = documentSnapshot.get(Data.USER_GROUPS) as ArrayList<String>
                    )

//                    getInvitations()
                    setNavigationHeader()
                }
        }
    }

    fun setNavigationHeader()
    {

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

    fun logOut()
    {
        var editor = getSharedPreferences(Data.SHAREDPREF, Context.MODE_PRIVATE).edit()
        editor.putString(Data.SAVEDUSER,"")

        editor.commit()

        var i = Intent(this,LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    fun confirmAndDelete()
    {
        var alert = AlertDialog.Builder(this)
        alert.setTitle("Delete Account")
        alert.setMessage("Your Data Will De Deleted Forever")

        alert.setPositiveButton("Delete",{dialog, which ->
            deleteAccount()
        })

        alert.setNegativeButton("Cancel",{dialog, which ->  })
        alert.show()
    }

    fun deleteAccount()
    {
        for(group in Data.currentUser.groups)
        {
            Data.db.collection(Data.GROUPS).document(group).get()
                .addOnSuccessListener { documentSnapshot ->
                    var users = documentSnapshot[Data.MEMBERS] as ArrayList<String>
                    users.remove(Data.currentUser.phone)

                    Log.e("TAG",users.toString())
                    var update = hashMapOf(
                        Data.MEMBERS to users
                    )

                    documentSnapshot.reference.set(update, SetOptions.merge())
                }
        }

        Data.db.collection(Data.USERS).document(Data.currentUser.phone).delete()
            .addOnSuccessListener {
                logOut()
                var i = Intent(this,LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            }
    }

}
