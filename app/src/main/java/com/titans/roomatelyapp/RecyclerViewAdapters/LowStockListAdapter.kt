package com.titans.roomatelyapp.RecyclerViewAdapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.SetOptions
import com.titans.roomatelyapp.Data
import com.titans.roomatelyapp.DataModels.Category
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.DataModels.Transaction
import com.titans.roomatelyapp.LowStockItems
import com.titans.roomatelyapp.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LowStockListAdapter: RecyclerView.Adapter<LowStockListAdapter.ViewHolder>
{
    var ctx: Context
    var lowStockItems: ArrayList<LowItem>

    constructor(ctx: Context, lowStockItems: ArrayList<LowItem>) : super()
    {
        this.ctx = ctx
        this.lowStockItems = lowStockItems
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        var view = LayoutInflater.from(ctx).inflate(R.layout.custom_low_stock_items,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int
    {
        Log.e("TAG","List size: "+lowStockItems.size)
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
                updateDatabase(lowStockItems[position])
            }
        })
        holder.txtItem.text = lowStockItems[position].item.name
        holder.txtCat.text = lowStockItems[position].cat.title

        holder.removeItem.setOnClickListener { v ->

            var alert = AlertDialog.Builder(ctx)
            alert.setTitle("Alert")
            alert.setMessage("Item will be set as In-Stock")
            alert.setIcon(R.drawable.low_stock)
            alert.setPositiveButton("Remove",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int)
                {
                    lowStockItems[position].item.inStock = true
                    holder.parent.startAnimation(animation)
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

    fun updateDatabase(item: LowItem)
    {

        if(Data.selectedGroup.equals("Self"))
        {
            var update = hashMapOf(
                item.item.name to item.item
            )
            Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("items").document(item.cat.title).set(update,
                SetOptions.merge())
                .addOnSuccessListener { void ->

                    var t = Transaction(
                        title = item.item.name+ " back in stock",
                        subTitle = "Marked By: "+Data.currentUser.name,
                        date = SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime())
                    )
                    Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("transactions")
                        .document(Data.getTimeStamp()).set(t)

                    lowStockItems.remove(item)
                    notifyDataSetChanged()
                }
        }
        else
        {

            var update = hashMapOf(
                item.item.name to item.item
            )
            Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1]).collection("items").document(item.cat.title).set(update,
                    SetOptions.merge())
                .addOnSuccessListener { void ->

                    var t = Transaction(
                        title = item.item.name+ " back in stock",
                        subTitle = "Marked By: "+Data.currentUser.name,
                        date = SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime())
                    )
                    Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1])
                        .collection("transactions").document(Data.getTimeStamp()).set(t)

                    lowStockItems.remove(item)
                    notifyDataSetChanged()
                }
        }
    }



    class ViewHolder: RecyclerView.ViewHolder
    {
        var parent: ConstraintLayout
        var txtItem: TextView
        var removeItem: ImageView
        var txtCat: TextView
        constructor(itemView: View) : super(itemView)
        {
            parent = itemView.findViewById(R.id.parent)
            txtItem = itemView.findViewById(R.id.txtItem)
            txtCat = itemView.findViewById(R.id.txtCategory)
            removeItem = itemView.findViewById(R.id.removeItem)
        }
    }

    class LowItem
    {
        val item:Item
        val cat: Category

        constructor(item: Item, cat: Category) {
            this.item = item
            this.cat = cat
        }
    }
}