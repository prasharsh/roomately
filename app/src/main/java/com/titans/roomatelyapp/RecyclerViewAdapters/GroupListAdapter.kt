package com.titans.roomatelyapp.RecyclerViewAdapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.titans.roomatelyapp.Data
import com.titans.roomatelyapp.Members
import com.titans.roomatelyapp.R
import kotlinx.android.synthetic.main.custom_group_list.view.*

class GroupListAdapter: RecyclerView.Adapter<GroupListAdapter.ViewHolder>
{
    var ctx: Context

    constructor(ctx: Context) : super()
    {
        this.ctx = ctx
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        var view = LayoutInflater.from(ctx).inflate(R.layout.custom_group_list,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int
    {
        return Data.currentUser.groups.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        Log.e("TAG",Data.currentUser.groups[position])
        var grp = Data.currentUser.groups[position].split(Data.CONCAT)[1]
        holder.txtGroup.setText(grp)

        holder.parent.setOnClickListener { v ->
            var membersActivity = Intent(ctx,Members::class.java)
            membersActivity.putExtra(Data.GROUPNAME,Data.currentUser.groups[position])
            ctx.startActivity(membersActivity)
        }
    }

    class ViewHolder: RecyclerView.ViewHolder
    {
        var txtGroup: TextView
        var parent: CardView
        constructor(itemView: View) : super(itemView)
        {
            txtGroup = itemView.findViewById(R.id.txtGroup)
            parent = itemView.findViewById(R.id.parent)
        }
    }
}