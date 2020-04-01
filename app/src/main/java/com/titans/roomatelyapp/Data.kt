package com.titans.roomatelyapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.titans.roomatelyapp.DataModels.Invitation
import com.titans.roomatelyapp.DataModels.Transaction
import com.titans.roomatelyapp.DataModels.User
import com.titans.roomatelyapp.RecyclerViewAdapters.GroupListAdapter
import com.titans.roomatelyapp.RecyclerViewAdapters.InvitationListAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Data {
    companion object
    {
        lateinit var db: FirebaseFirestore
        var groupListAdapter: GroupListAdapter? = null
        var invitationListAdapter: InvitationListAdapter? = null
        var currentUser: User = User()
        var groups = ArrayList<String>()
        var invitations = ArrayList<Invitation>()
        var txtInvitations: MutableLiveData<Int> = MutableLiveData()
        var selectedGroup = ""
        var started = false


        fun init()
        {
            db = FirebaseFirestore.getInstance()
        }

        fun setTranscationListners(ctx: Context)
        {
            for(group in currentUser.groups)
            {
                db.collection(GROUPS).document(group).collection("transactions")
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        if(firebaseFirestoreException!=null)
                            return@addSnapshotListener

                        Log.e("TAG","New Activity in "+group)

                        if(started)
                        {

                            var nManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                            var builder = NotificationCompat.Builder(ctx, ctx.packageName)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle("New Activity in Group "+group.split(CONCAT)[1])
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                            nManager.notify(100,builder.build())

                        }

                        if(currentUser.groups.indexOf(group)== currentUser.groups.lastIndex)
                            started = true
                    }
            }
        }

        fun getGroupAdapter(ctx: Context): GroupListAdapter
        {
            groupListAdapter = GroupListAdapter(ctx)
            return groupListAdapter as GroupListAdapter
        }

        fun getInvitationAdapter(ctx: Context): InvitationListAdapter
        {
            invitationListAdapter = InvitationListAdapter(ctx)
            return invitationListAdapter as InvitationListAdapter
        }

        fun getInvitations(ctx: Context) {
            Log.e("TAG", "USER: ${currentUser.phone}")
            db.collection(USERS).document(currentUser.phone).get()
                .addOnSuccessListener { documentSnapshot ->
                    var list = documentSnapshot["invitations"]

                    if (list != null) {
                        invitations.clear()
                        for (invitation in (list as ArrayList<HashMap<String, String>>)) {
                            invitations.add(
                                Invitation(
                                    group = invitation["group"]!!,
                                    phone = invitation["phone"]!!
                                )
                            )
                        }
                        txtInvitations.value = list.size
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(ctx, "Error Getting Invitations", Toast.LENGTH_LONG).show()
                }
        }

        fun createGroupList()
        {
            groups.clear()
            groups.add("Self")
            for(group in currentUser.groups)
            {
                groups.add(group.split(CONCAT)[1])
            }
        }

        fun getTimeStamp(): String
        {
            return SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
        }

        fun updateGroups()
        {
            db.collection(USERS).document(currentUser.phone).get()
                .addOnSuccessListener { documentSnapshot ->
                    currentUser.groups = documentSnapshot.get(USER_GROUPS) as ArrayList<String>
                    createGroupList()

                    if(!groups.contains(selectedGroup))
                        selectedGroup = "Self"
                }
        }



        //constants
        val CONCAT = "_ _"
        val CONCATCOORD = "_/_"
        val GROUPNAME = "groupName"

        val GROUPS = "groups"
        val USERS = "users"
        val MEMBERS = "members"

        val USER_NAME = "name"
        val USER_PHONE = "phone"
        val USER_GROUPS = "groups"
        val USER_PASS = "password"

        val SHAREDPREF = "cookie"
        val SAVEDUSER = "user"
    }
}