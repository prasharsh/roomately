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
import com.google.firebase.firestore.SetOptions
import com.titans.roomatelyapp.Data
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.DataModels.Transaction
import com.titans.roomatelyapp.R
import kotlinx.android.synthetic.main.actionbar.*
import kotlinx.android.synthetic.main.activity_item_crud.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ItemsActivity: AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_crud)

        backButton.setOnClickListener { _ -> onBackPressed() }
        txtToolbarLabel.text = Data.selectedGroup+" > "+getString(R.string.add_item)
        checkValues()

        saveItemFloatingButton.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        if (ETProductName.text.length==0 || ETDesc.text.length==0 || ETProductCategory.text.length==0)
        {
            showError()
            Toast.makeText(baseContext, "Provide name / desc / category details", Toast.LENGTH_SHORT).show()
        }
        else {
            /* Code for adding item to Firebase */
            val name: String = ETProductName.text.toString()
            val desc: String = ETDesc.text.toString()
            val status = checkStatus.isChecked
            val category = ETProductCategory.text.toString()
            var barcode = txtBarcode.text.toString()

            val item = Item(name=name,desc = desc,locations = ArrayList<String>(),inStock = status,barcodes = getBarcodes(barcode))

            var i = hashMapOf(name to item)



            if(Data.selectedGroup.equals("Self"))
            {
                Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("items").document(category).set(i,
                        SetOptions.merge())
                    .addOnSuccessListener { void ->


                        var transaction = Transaction(
                            title = "Item Added: "+name,
                            subTitle = "Added By : "+Data.currentUser.name,
                            date = SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime())
                        )

                        Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("transactions")
                            .document(Data.getTimeStamp()).set(transaction)

                        Toast.makeText(this,"Item Added",Toast.LENGTH_LONG).show()
                        onBackPressed()}
                    .addOnFailureListener { exception -> Toast.makeText(this,"Error Adding Item",Toast.LENGTH_LONG).show() }

            }
            else
            {
                Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1]).collection("items").document(category).set(i,
                        SetOptions.merge())
                    .addOnSuccessListener { void ->

                        var transaction = Transaction(
                            title = "Item Added: "+name,
                            subTitle = "Added By : "+Data.currentUser.name,
                            date = SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime())
                        )

                        Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1]).collection("transactions")
                            .document(Data.getTimeStamp()).set(transaction)

                        Toast.makeText(this,"Item Added",Toast.LENGTH_LONG).show()
                        onBackPressed()}
                    .addOnFailureListener { exception -> Toast.makeText(this,"Error Adding Item",Toast.LENGTH_LONG).show() }
            }

        }
    }

    private fun showError()
    {
        if(ETProductName.text.length==0){
            ETProductName.error= "Product name cannot be blank."
        }
        if(ETDesc.text.length==0){
            ETDesc.error= "Product description cannot be blank."
        }
        if(ETProductCategory.text.length==0){
            ETProductCategory.error= "Product category cannot be blank."
        }
    }

    private fun checkValues()
    {
        ETProductName.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable)
            {
                if (s.isNullOrBlank())
                {
                    ETProductName.error = "Product name cannot be null"
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {
                if (s.isNullOrBlank())
                {
                    ETProductName.error = "Product name cannot be null"
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
                if (s.isNullOrBlank())
                {
                    ETProductName.error = "Product name cannot be null"
                }
            }
        })

        ETDesc.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable)
            {
                if (s.isNullOrBlank())
                {
                    ETDesc.error = "Product description cannot be null"
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {
                if (s.isNullOrBlank())
                {
                    ETDesc.error = "Product description cannot be null"
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
                if (s.isNullOrBlank())
                {
                    ETDesc.error = "Product description cannot be null"
                }
            }
        })

        ETProductCategory.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable)
            {
                if (s.isNullOrBlank())
                {
                    ETProductCategory.error = "Product category cannot be null"
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {
                if (s.isNullOrBlank())
                {
                    ETProductCategory.error = "Product category cannot be null"
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
                if (s.isNullOrBlank())
                {
                    ETProductCategory.error = "Product category cannot be null"
                }
            }
        })
    }

    fun getBarcodes(barcodeString: String): ArrayList<String>
    {
        var barcodes = ArrayList<String>()

        if(barcodeString.equals(""))
            return barcodes

        var barcodeArray = barcodeString.split("\n")

        for(index in 1..barcodeArray.size-1)
        {
            barcodes.add(barcodeArray[index])
        }

        return barcodes
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