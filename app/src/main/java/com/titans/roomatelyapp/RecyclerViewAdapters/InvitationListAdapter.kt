package com.titans.roomatelyapp.RecyclerViewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.SetOptions
import com.titans.roomatelyapp.Data
import com.titans.roomatelyapp.R
import kotlinx.android.synthetic.main.custom_invitation_list.view.*

class InvitationListAdapter: RecyclerView.Adapter<InvitationListAdapter.ViewHolder>
{
    var ctx: Context

    constructor(ctx: Context) : super() {
        this.ctx = ctx
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.custom_invitation_list,parent,false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return Data.invitations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txtGroup.text = Data.invitations[position].group.split(Data.CONCAT)[1]
        holder.txtMember.text = Data.invitations[position].phone

        holder.acceptButton.setOnClickListener { v ->
            var i = Data.invitations.removeAt(position)

            Data.currentUser.groups.add(i.group)
            var update = hashMapOf(
                "invitations" to Data.invitations,
                Data.GROUPS to Data.currentUser.groups
            )

            Data.db.collection(Data.USERS).document(Data.currentUser.phone).set(update, SetOptions.merge())
                .addOnSuccessListener { void ->

                    Data.db.collection(Data.GROUPS).document(i.group).get()
                        .addOnSuccessListener { documentSnapshot ->
                            var members = documentSnapshot[Data.MEMBERS] as ArrayList<String>

                            members.add(Data.currentUser.phone)

                            documentSnapshot.reference.set(hashMapOf(Data.MEMBERS to members),
                                SetOptions.merge())
                        }


                    Data.groups.add(i.group.split(Data.CONCAT)[1])
                    Toast.makeText(ctx,"You are added to group",Toast.LENGTH_LONG).show()
                    notifyDataSetChanged()
                    Data.txtInvitations.value = Data.invitations.size
                    Data.groupListAdapter?.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(ctx,"Error Adding to Group",Toast.LENGTH_LONG).show()
                }
        }

        holder.rejectButton.setOnClickListener { v ->
            var i = Data.invitations.removeAt(position)

            var invites = hashMapOf(
                "invitations" to Data.invitations
            )
            Data.db.collection(Data.USERS).document(Data.currentUser.phone).set(invites, SetOptions.merge())
                .addOnSuccessListener { void ->

                    Toast.makeText(ctx,"Invitation Rejected",Toast.LENGTH_LONG).show()
                    Data.txtInvitations.value = Data.invitations.size
                    notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(ctx,"Error Rejecting Invitation",Toast.LENGTH_LONG).show()
                }
        }
    }

    class ViewHolder:RecyclerView.ViewHolder
    {
        var txtGroup: TextView
        var txtMember: TextView
        var acceptButton: ImageButton
        var rejectButton: ImageButton
        constructor(itemView: View) : super(itemView)
        {
            txtGroup = itemView.findViewById(R.id.txtGroup)
            txtMember =  itemView.findViewById(R.id.txtMember)
            acceptButton =  itemView.findViewById(R.id.acceptButton)
            rejectButton =  itemView.findViewById(R.id.rejectButton)
        }
    }
}