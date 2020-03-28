package com.titans.roomatelyapp.RecyclerViewAdapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.titans.roomatelyapp.DataModels.Category
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.R

class LowStockListAdapter: RecyclerView.Adapter<LowStockListAdapter.ViewHolder>
{
    var ctx: Context
    var categories: ArrayList<Category>
    var lowStockItems =  ArrayList<Item>()

    constructor(ctx: Context, categories: ArrayList<Category>) : super()
    {
        this.ctx = ctx
        this.categories = categories
        findLowStockItems()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        var view = LayoutInflater.from(ctx).inflate(R.layout.custom_low_stock_items,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int
    {
        return lowStockItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        var animation = AnimationUtils.loadAnimation(ctx, R.anim.swipe_right)
        animation.setAnimationListener(object: Animation.AnimationListener
        {
            override fun onAnimationRepeat(animation: Animation?)
            {
            }

            override fun onAnimationStart(animation: Animation?)
            {
            }

            override fun onAnimationEnd(animation: Animation?)
            {
                    lowStockItems.remove(lowStockItems[position])
                    notifyDataSetChanged()
            }
        })
        holder.txtItem.text = lowStockItems[position].name

        holder.removeItem.setOnClickListener { v ->

            var alert = AlertDialog.Builder(ctx)
            alert.setTitle("Alert")
            alert.setMessage("Item will be set as In-Stock")
            alert.setIcon(R.drawable.low_stock)
            alert.setPositiveButton("Remove",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int)
                {
                    holder.parent.startAnimation(animation)
                    lowStockItems[position].inStock = true
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

    fun findLowStockItems()
    {
        for(category in categories)
        {
            for(item in category.items)
            {
                if(!item.inStock)
                {
                    lowStockItems.add(item)
                }
            }

        }
    }

    class ViewHolder: RecyclerView.ViewHolder
    {
        var parent: ConstraintLayout
        var txtItem: TextView
        var removeItem: ImageView
        constructor(itemView: View) : super(itemView)
        {
            parent = itemView.findViewById(R.id.parent)
            txtItem = itemView.findViewById(R.id.txtItem)
            removeItem = itemView.findViewById(R.id.removeItem)
        }
    }
}