package com.titans.roomatelyapp.RecyclerViewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.titans.roomatelyapp.DataModels.Transaction
import com.titans.roomatelyapp.R

class TransactionsAdapter: RecyclerView.Adapter<TransactionsAdapter.ViewHolder>
{
    var ctx: Context
    var transactions: ArrayList<Transaction>

    constructor(ctx: Context, transactions: ArrayList<Transaction>) : super()
    {
        this.ctx = ctx
        this.transactions = transactions
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        var view = LayoutInflater.from(ctx).inflate(R.layout.custom_transaction,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int
    {
        return transactions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.txtTitle.text = transactions[position].title
        holder.txtSubTitle.text = transactions[position].subTitle
        holder.txtDate.text = transactions[position].date

//        if(transactions[position].action == Transaction.IN_STOCK)
//        {
//            holder.txtUser.text="Marked in-stock by "+transactions[position].user
//            holder.txtUser.setTextColor(ctx.resources.getColor(R.color.green))
//            holder.txtProduct.setTextColor(ctx.resources.getColor(R.color.green))
//        }
//        else
//        {
//            holder.txtUser.text="Marked low-stock by "+transactions[position].user
//            holder.txtUser.setTextColor(ctx.resources.getColor(R.color.red))
//            holder.txtProduct.setTextColor(ctx.resources.getColor(R.color.red))
//        }
    }

    class ViewHolder:RecyclerView.ViewHolder
    {
        var txtTitle: TextView
        var txtSubTitle: TextView
        var txtDate: TextView

        constructor(itemView: View) : super(itemView)
        {
            this.txtTitle = itemView.findViewById(R.id.txtTitle)
            this.txtSubTitle = itemView.findViewById(R.id.txtSubTitle)
            this.txtDate = itemView.findViewById(R.id.txtDate)
        }
    }
}