package com.titans.roomatelyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.titans.roomatelyapp.DataModels.Category
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.RecyclerViewAdapters.CategoryListAdapter
import com.titans.roomatelyapp.RecyclerViewAdapters.ItemListAdapter
import kotlinx.android.synthetic.main.activity_items.*

class Items : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        var backButton = findViewById<ImageButton>(R.id.backButton)
        var txtToolbarLabel = findViewById<TextView>(R.id.txtToolbarLabel)

        backButton.setOnClickListener { v -> onBackPressed() }
        txtToolbarLabel.text = "Group Name"+" > Items"

        var adapter = CategoryListAdapter(this,getData())
        categoryList.adapter = adapter
        categoryList.layoutManager = LinearLayoutManager(this)
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
