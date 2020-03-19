package com.titans.roomatelyapp.RecyclerViewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.R

class ItemListAdapter:RecyclerView.Adapter<ItemListAdapter.ViewHolder>
{
    lateinit var ctx:Context
    var items: List<Item>

    constructor(ctx: Context, items: List<Item>) : super()
    {
        this.ctx = ctx
        this.items = items
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        var view = LayoutInflater.from(ctx).inflate(R.layout.custom_items_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.txtItem.text = items[position].name

        if(items[position].inStock)
            holder.itemStatusIcon.setImageResource(R.drawable.in_stock_green)
        else
            holder.itemStatusIcon.setImageResource(R.drawable.low_stock_red)
    }

    override fun getItemCount(): Int
    {
        return items.size
    }

    class ViewHolder: RecyclerView.ViewHolder
    {
        lateinit var txtItem: TextView
        lateinit var itemStatusIcon: ImageView
        constructor(itemView: View) : super(itemView)
        {
            txtItem = itemView.findViewById<TextView>(R.id.txtItem)
            itemStatusIcon = itemView.findViewById<ImageView>(R.id.item_status_icon)
        }
    }
}