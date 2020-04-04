package com.titans.roomatelyapp.MainWindowTabs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.titans.roomatelyapp.Data
import com.titans.roomatelyapp.Invitations
import com.titans.roomatelyapp.R
import com.titans.roomatelyapp.dialogs.CreateGroupDialog

class Groups:Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        var view = inflater.inflate(R.layout.tab_groups,container,false)

        var groupList = view.findViewById<RecyclerView>(R.id.groupList)
        var createGroupButton = view.findViewById<FloatingActionButton>(R.id.addGroupButton)
        var cardInvite = view.findViewById<CardView>(R.id.cardInvite)
        var txtNumInvitation = view.findViewById<TextView>(R.id.txtNumInvitations)

        groupList.adapter = Data.getGroupAdapter(requireContext())
        groupList.layoutManager = LinearLayoutManager(requireContext())


        createGroupButton.setOnClickListener { v ->
            var createGroupDialog = CreateGroupDialog()
            createGroupDialog.show(requireFragmentManager(),"CreateGroupDialog")

        }

        cardInvite.setOnClickListener { v ->
            startActivity(Intent(context,Invitations::class.java))
        }

        Data.txtInvitations.observeForever { t ->
            txtNumInvitation.setText(t.toString())
        }

        return view;
    }

}