package com.titans.roomatelyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.titans.roomatelyapp.items.ItemsActivity
import com.titans.roomatelyapp.login.RegistrationActivity

class Items : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        var backButton = findViewById<ImageButton>(R.id.backButton)
        var txtToolbarLabel = findViewById<TextView>(R.id.txtToolbarLabel)
        val navToAddItem = findViewById<FloatingActionButton>(R.id.addItemFloatingButtoon)
        backButton.setOnClickListener { _ -> onBackPressed() }
        txtToolbarLabel.text = "Group Name"+" > Items"

        navToAddItem.setOnClickListener{v ->
            startActivity(Intent(v.context, ItemsActivity::class.java))}
    }
}
