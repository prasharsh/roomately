package com.titans.roomatelyapp.items

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
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
    private var productName: EditText?= null
    private var productDesc: EditText?= null
    private var productCategory: EditText?= null
    private var switchStockStatus: Switch?= null
    private var productLocation: EditText?= null
    lateinit var backButton: ImageButton
    lateinit var saveBtn: FloatingActionButton
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_crud)

        productName = findViewById(R.id.ETProductName)
        productDesc = findViewById(R.id.ETDesc)
        productCategory = findViewById(R.id.ETProductCategory)
        switchStockStatus = findViewById(R.id.switchStockStatus)
        productLocation = findViewById(R.id.ETProductLocation)
        saveBtn = findViewById(R.id.saveItemFloatingButton)
        backButton = findViewById<ImageButton>(R.id.backButton)
        backButton?.setOnClickListener { _ -> onBackPressed() }
        val txtToolbarLabel = findViewById<TextView>(R.id.txtToolbarLabel)
        txtToolbarLabel.text = getString(R.string.add_item)
        checkValues()

        saveBtn.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        if (productName?.text?.length==0 || productDesc?.text?.length==0
            || productCategory?.text?.length==0) {
            showError()
            Toast.makeText(baseContext, "Provide name / desc / category details", Toast.LENGTH_SHORT).show()
        }
        else {
            /* Code for adding item to Firebase */
            val name: String = findViewById<EditText>(R.id.ETProductName).text.toString()
            val desc: String? = findViewById<EditText>(R.id.ETDesc).text.toString()
            val location: String? = findViewById<EditText>(R.id.ETProductLocation).text.toString()
            val lowStock = findViewById<Switch>(R.id.switchStockStatus).isChecked
            val item = Item(name, desc, lowStock, location)
            val ref = FirebaseFirestore.getInstance().collection("profiles")
                .document(user?.uid.toString()).collection("items").document(name)
            ref.set(item)
            finish()
            Toast.makeText(baseContext, "Data Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError()
    {
        if(productName?.text?.length==0){
            productName?.error= "Product name cannot be blank."
        }
        if(productDesc?.text?.length==0){
            productDesc?.error= "Product description cannot be blank."
        }
        if(productCategory?.text?.length==0){
            productCategory?.error= "Product category cannot be blank."
        }
    }

    private fun checkValues()
    {
        productName!!.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                if (s.isNullOrBlank())
                {
                    productName?.error = "Product name cannot be null"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
                if (s.isNullOrBlank())
                {
                    productName?.error = "Product name cannot be null"
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                if (s.isNullOrBlank())
                {
                    productName?.error = "Product name cannot be null"
                }
            }
        })

        productDesc!!.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                if (s.isNullOrBlank())
                {
                    productDesc?.error = "Product description cannot be null"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
                if (s.isNullOrBlank())
                {
                    productDesc?.error = "Product description cannot be null"
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                if (s.isNullOrBlank())
                {
                    productDesc?.error = "Product description cannot be null"
                }
            }
        })

        productCategory!!.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                if (s.isNullOrBlank())
                {
                    productCategory?.error = "Product category cannot be null"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
                if (s.isNullOrBlank())
                {
                    productCategory?.error = "Product category cannot be null"
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                if (s.isNullOrBlank())
                {
                    productCategory?.error = "Product category cannot be null"
                }
            }
        })
    }

    /* Hide keyboard when user touches outside of EditText. */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}