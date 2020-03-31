package com.titans.roomatelyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.titans.roomatelyapp.DataModels.Invitation
import com.titans.roomatelyapp.RecyclerViewAdapters.InvitationListAdapter
import kotlinx.android.synthetic.main.actionbar.*
import kotlinx.android.synthetic.main.activity_invitations.*

class Invitations : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitations)

        backButton.setOnClickListener { v -> onBackPressed() }
        txtToolbarLabel.text = "Invitations"
        Data.getInvitations(this)
        invitationList.adapter = Data.getInvitationAdapter(this)
        invitationList.layoutManager = LinearLayoutManager(this)
    }

}
