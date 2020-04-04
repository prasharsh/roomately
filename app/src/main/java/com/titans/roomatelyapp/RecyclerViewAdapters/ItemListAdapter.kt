package com.titans.roomatelyapp.RecyclerViewAdapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.R
import com.titans.roomatelyapp.dialogs.ProductDetailDialog

class ItemListAdapter:RecyclerView.Adapter<ItemListAdapter.ViewHolder>
{
    lateinit var ctx:Context
    var items: ArrayList<Item>
    var category: String

    constructor(ctx: Context, items: ArrayList<Item>, category: String) : super() {
        this.ctx = ctx
        this.items = items
        this.category = category
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

        holder.parent.setOnClickListener { v ->
            val productDialog = ProductDetailDialog(item = items[position],adapter = this)
            productDialog.show((ctx as AppCompatActivity).supportFragmentManager,"Product Details")
        }
    }

    override fun getItemCount(): Int
    {
        return items.size
    }

    class ViewHolder: RecyclerView.ViewHolder
    {
        lateinit var txtItem: TextView
        lateinit var itemStatusIcon: ImageView
        var parent: CardView
        constructor(itemView: View) : super(itemView)
        {
            txtItem = itemView.findViewById(R.id.txtItem)
            itemStatusIcon = itemView.findViewById(R.id.item_status_icon)
            parent = itemView.findViewById(R.id.parent)
        }
    }
}