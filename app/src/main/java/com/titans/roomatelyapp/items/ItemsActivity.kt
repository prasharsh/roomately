package com.titans.roomatelyapp.items

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.titans.roomatelyapp.R
import com.titans.roomatelyapp.model.Item
import kotlinx.android.synthetic.main.activity_item_crud.*

class ItemsActivity: AppCompatActivity()
{
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_crud)
        val submitButton = findViewById<FloatingActionButton>(R.id.saveItemFloatingButton)
        // TODO: link below button to barcode scanner.
        val addImage = findViewById<FloatingActionButton>(R.id.barcodeScannerFloatingButton)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener { onBackPressed() }

        /* Code for adding item to Firebase */
        submitButton.setOnClickListener {
            val name: String? = findViewById<EditText>(R.id.ETProductName).text.toString()

            /* Check if user forgot product name */
            if (name == null || name == "") {
                findViewById<EditText>(R.id.ETProductName).error = "Please enter an item name."
            }
            else {
                val desc: String? = findViewById<EditText>(R.id.ETDesc).text.toString()
                val location: String? = findViewById<EditText>(R.id.ETProductLocation).text.toString()
                val lowStock = findViewById<Switch>(R.id.switchToggle).isChecked
                val item = Item(name, desc, lowStock, location)
                Toast.makeText(baseContext, "else statement",
                    Toast.LENGTH_SHORT).show()

                val ref = FirebaseFirestore.getInstance().collection("profiles")
                    .document(user?.uid.toString()).collection("items").document(name)

                ref.set(item)
                finish()
            }
        }
    }


}