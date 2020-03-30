package com.titans.roomatelyapp.RecyclerViewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.titans.roomatelyapp.DataModels.Category
import com.titans.roomatelyapp.R

class CategoryListAdapter: RecyclerView.Adapter<CategoryListAdapter.ViewHolder>
{
    var ctx: Context;
    var categoryList: List<Category>
    var drop = false

    constructor(ctx: Context, categoryList: List<Category>) : super()
    {
        this.ctx = ctx
        this.categoryList = categoryList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        var view = LayoutInflater.from(ctx).inflate(R.layout.custom_categories_list,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        var clockwiseRotation = AnimationUtils.loadAnimation(ctx,R.anim.rotate_clockwise)
        var antiClockwiseRotation = AnimationUtils.loadAnimation(ctx,R.anim.rotate_anticlockwise)

        var dropDown = AnimationUtils.loadAnimation(ctx, R.anim.slide_down)
        var dropUp = AnimationUtils.loadAnimation(ctx, R.anim.slide_up)

        holder.txtCategory.text = categoryList[position].title

        holder.txtCategory.setOnClickListener { v ->
            if(!drop)
            {
                holder.itemList.visibility = View.VISIBLE
                holder.itemsContainer.startAnimation(dropDown)
                holder.imageDrop.startAnimation(clockwiseRotation)
                drop=!drop
            }
            else
            {drop=!drop

                holder.itemList.visibility = View.GONE
//                Toast.makeText(ctx,holder.itemList.scaleY.toString(),Toast.LENGTH_LONG).show()
                holder.itemsContainer.startAnimation(dropUp)
                holder.imageDrop.startAnimation(antiClockwiseRotation)
            }
        }

        var itemsAdapter = ItemListAdapter(ctx,categoryList[position].items,categoryList[position].title)

        holder.itemList.adapter = itemsAdapter
        holder.itemList.layoutManager = LinearLayoutManager(ctx)
    }

    override fun getItemCount(): Int
    {
        return categoryList.size
    }

    class ViewHolder: RecyclerView.ViewHolder
    {
        var txtCategory: TextView
        var itemList: RecyclerView
        var imageDrop: ImageView
        lateinit var itemsContainer:CardView
        constructor(itemView: View) : super(itemView)
        {
            txtCategory = itemView.findViewById<TextView>(R.id.txtCategory)
            itemList = itemView.findViewById<RecyclerView>(R.id.itemsList)
            imageDrop = itemView.findViewById<ImageView>(R.id.imageDrop)
            itemsContainer = itemView.findViewById(R.id.itemsContainer)
        }
    }

}