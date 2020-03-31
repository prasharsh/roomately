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
        getInvitations()
        invitationList.adapter = Data.getInvitationAdapter(this)
        invitationList.layoutManager = LinearLayoutManager(this)
    }

    fun getInvitations()
    {
        Data.db.collection(Data.USERS).document(Data.currentUser.phone).get()
            .addOnSuccessListener { documentSnapshot ->
                var list = documentSnapshot["invitations"]

                if(list != null) {
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
