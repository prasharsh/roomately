package com.titans.roomatelyapp.MainWindowTabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.titans.roomatelyapp.Items
import com.titans.roomatelyapp.LowStockItems
import com.titans.roomatelyapp.R
import com.titans.roomatelyapp.Transactions

class Dashboard: Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        var view = inflater.inflate(R.layout.tab_dashboard,container,false)

        var cardItems = view.findViewById<CardView>(R.id.cardItems)
        var cardLowStock = view.findViewById<CardView>(R.id.cardLowStock)
        var cardTransaction = view.findViewById<CardView>(R.id.cardTransaction)

        cardItems.setOnClickListener{v ->
            startActivity(Intent(view.context, Items::class.java))
        }

        cardLowStock.setOnClickListener{v ->
            startActivity(Intent(view.context, LowStockItems::class.java))
        }

        cardTransaction.setOnClickListener{v ->
            startActivity(Intent(view.context, Transactions::class.java))
        }

        return view;
    }
}