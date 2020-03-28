package com.titans.roomatelyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.titans.roomatelyapp.DataModels.Category
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.RecyclerViewAdapters.LowStockListAdapter
import kotlinx.android.synthetic.main.activity_low_stock_items.*

class LowStockItems : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_low_stock_items)

        var backButton = findViewById<ImageButton>(R.id.backButton)
        var txtToolbarLabel = findViewById<TextView>(R.id.txtToolbarLabel)

        backButton.setOnClickListener { v -> onBackPressed() }
        txtToolbarLabel.text = "Group Name"+" > Low Stock"

        var adapter = LowStockListAdapter(this,getData())
        lowStockItemsList.adapter = adapter
        lowStockItemsList.layoutManager = LinearLayoutManager(this)

    }
    fun getData(): ArrayList<Category>
    {
        var list = ArrayList<Category>()
        for(j in 1..3)
        {
            var items = ArrayList<Item>()
            for(i in 1..10)
            {
                var item = Item("Item$i",i%2==0)
                items.add(item)
            }
            list.add(Category("Category$j",items))
        }
        return list
    }
}
