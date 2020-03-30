package com.titans.roomatelyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.titans.roomatelyapp.DataModels.User
import com.titans.roomatelyapp.RecyclerViewAdapters.MemberListAdapter
import com.titans.roomatelyapp.dialogs.AddMemberDialog
import kotlinx.android.synthetic.main.activity_members.*

class Members : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)

        var grp = intent.extras?.get(Data.GROUPNAME) as String

        txtGroup.text = grp.split(Data.CONCAT)[1]

        var members = ArrayList<User>()
        var adapter = MemberListAdapter(this,grp,members)

        membersList.adapter = adapter
        membersList.layoutManager = LinearLayoutManager(this)

        //get members from database and update adapter
        getMembers(grp,members,adapter)



        //floating action button clicked
        addMemberButton.setOnClickListener { v ->
            var addMember = AddMemberDialog(grp,this)
            addMember.show(supportFragmentManager,"Add Member")
        }
    }

    fun getMembers(grp: String,members: ArrayList<User>,adapter: MemberListAdapter)
    {
        Data.db.collection(Data.GROUPS).document(grp).get()
            .addOnSuccessListener { documentSnapshot ->

                for(member in documentSnapshot.get(Data.MEMBERS) as ArrayList<String>)
                {
                    Data.db.collection(Data.USERS).document(member).get()
                        .addOnSuccessListener { documentSnapshot ->
                            var user = User(
                                name = documentSnapshot.getString(Data.USER_NAME)!!,
                                phone = documentSnapshot.getString(Data.USER_PHONE)!!,
                                groups = documentSnapshot.get(Data.USER_GROUPS) as ArrayList<String>
                            )

                            members.add(user)
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener{ exception -> Toast.makeText(this,"Error Getting Member",Toast.LENGTH_LONG).show() }
                }
            }
            .addOnFailureListener{ exception -> Toast.makeText(this,"Error Getting Members",Toast.LENGTH_LONG).show() }
    }
}
