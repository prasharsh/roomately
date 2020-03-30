package com.titans.roomatelyapp.RecyclerViewAdapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.SetOptions
import com.titans.roomatelyapp.Data
import com.titans.roomatelyapp.DataModels.User
import com.titans.roomatelyapp.R
import kotlinx.android.synthetic.main.custom_member_list.view.*

class MemberListAdapter: RecyclerView.Adapter<MemberListAdapter.ViewHolder>
{
    var ctx: Context
    var group: String
    var users: ArrayList<User>

    constructor(ctx: Context, group: String, users: ArrayList<User>) : super() {
        this.ctx = ctx
        this.group = group
        this.users = users
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.custom_member_list,parent,false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtMember.text = users[position].name
        holder.txtPhoneNumber.text = users[position].phone

        if(Data.currentUser.phone.equals(users[position].phone))
        {
            holder.removeButton.visibility = View.GONE
        }

        holder.removeButton.setOnClickListener { v ->

            var alert = AlertDialog.Builder(ctx)
            alert.setTitle("Alert")
            alert.setMessage(users[position].name+" will be removed from the group")
            alert.setIcon(R.drawable.low_stock)
            alert.setPositiveButton("Remove",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int)
                {
                    var user = users.removeAt(position)
                    user.groups.remove(group)
                    removeGroupFromUser(user)
                    removeUserFromGroup(user)
                    notifyDataSetChanged()
                }
            })

            alert.setNegativeButton("Cancel",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int)
                {
                }
            })

            alert.show()
        }
    }

    fun removeGroupFromUser(user: User)
    {
        var newGroups = hashMapOf(
            Data.GROUPS to user.groups
        )
        Data.db.collection(Data.USERS).document(user.phone).set(newGroups, SetOptions.merge())
    }

    fun removeUserFromGroup(user: User)
    {
        var users = ArrayList<String>()

        for(u in this.users)
        {
            if(!u.phone.equals(user.phone))
                users.add(u.phone)
        }
        var newUsers = hashMapOf(
            Data.MEMBERS to users
        )
        Data.db.collection(Data.GROUPS).document(group).set(newUsers, SetOptions.merge())
    }

    class ViewHolder: RecyclerView.ViewHolder
    {
        var txtMember: TextView
        var txtPhoneNumber: TextView
        var removeButton: ImageButton

        constructor(itemView: View) : super(itemView)
        {
            txtMember = itemView.findViewById(R.id.txtMember)
            txtPhoneNumber = itemView.findViewById(R.id.txtPhoneNumber)
            removeButton = itemView.findViewById(R.id.removeButton)
        }
    }
}