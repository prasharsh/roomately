package com.titans.roomatelyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.titans.roomatelyapp.DataModels.Category
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.RecyclerViewAdapters.CategoryListAdapter
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

        var categories : List<Category> = getList()

        var adapter = CategoryListAdapter(this@Items,categories)

        categoryList.adapter = adapter
        categoryList.layoutManager = LinearLayoutManager(this@Items)

        var animation = AnimationUtils.loadLayoutAnimation(this@Items,R.anim.layout_animation_fall_down)
        categoryList.layoutAnimation = animation
        categoryList.scheduleLayoutAnimation()
    }

    fun getList():ArrayList<Category>
    {
        var list = ArrayList<Category>()
        for(i in 1..10)
        {
            var items = ArrayList<Item>()
            for(j in 1..10)
            {
                items.add(Item("Item$j",i%2==0))
            }

            list.add(Category("Category$i",items))
        }
        return list
    }
}
