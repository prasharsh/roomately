package com.titans.roomatelyapp.login

import android.Manifest.permission.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.titans.roomatelyapp.Data
import com.titans.roomatelyapp.MainActivity
import com.titans.roomatelyapp.R
import kotlinx.android.synthetic.main.activity_registration.*
import java.io.Serializable


class RegistrationActivity : AppCompatActivity()
{
    private val TAG = "RegistrationActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val nameField = findViewById<EditText>(R.id.reg_first_name)
        val passField = findViewById<EditText>(R.id.reg_pass)
        val reenterPassField = findViewById<EditText>(R.id.reg_reenter_pass)
        val phoneField = findViewById<EditText>(R.id.reg_phone)
        val regButton = findViewById<Button>(R.id.reg_button)

        /* Navigate back to login. */
        val navToLogin = findViewById<TextView>(R.id.login_link)
        navToLogin.setOnClickListener{v ->
//            startActivity(Intent(v.context, LoginActivity::class.java))
            onBackPressed()
        }

        /* Code to register with email. */
        regButton.setOnClickListener {
            val name = nameField.text.toString().trim()
            val password = passField.text.toString()
            val reenterPass = reenterPassField.text.toString()
            val phone = phoneField.text.toString().trim()

            if(name.equals(""))
            {
                nameField.error = "Please enter name"
                return@setOnClickListener
            }
//            if(userExists(phone))
//            {
//                phoneField.error = "Already Registered"
//                return@setOnClickListener
//            }
            /* Validate user input before processing. */
            if (checkPassword(password, reenterPass)) {
                var user = hashMapOf(
                    "name" to name,
                    "password" to password,
                    "phone" to phone,
                    "groups" to arrayListOf<String>(),
                    "invitations" to arrayListOf<String>()
                )

                checkUserExistsAndCreateUser(user)
            }
        }
        readPhoneNumber()
    }

    fun readPhoneNumber()
    {

        if (ActivityCompat.checkSelfPermission(this,READ_SMS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                READ_PHONE_NUMBERS
            ) ==
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val tMgr =
                this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val mPhoneNumber = tMgr.line1Number
            if(mPhoneNumber!=null && !mPhoneNumber.equals(""))
            {
                reg_phone.setText(mPhoneNumber)
                reg_phone.isEnabled = false
            }
//            Toast.makeText(this@RegistrationActivity,"Phone number: "+mPhoneNumber,Toast.LENGTH_LONG).show()
        } else {
            requestPermission()
        }
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> {

                if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readPhoneNumber()
                } else {
                    Toast.makeText(this@RegistrationActivity,"Denied",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * @param pass string pulled from password field.
     * @param rePass re-entered user password.
     * @return Boolean indicating pass or fail.
     */
    private fun checkPassword(pass: String, rePass: String): Boolean {
        //check password constrains
        if(!pass.matches(Regex("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}\$")))
        {
            findViewById<EditText>(R.id.reg_pass).error = "Password Does Satisfy Conditions"
            return false
        }
        if (pass.isEmpty()) {
            findViewById<EditText>(R.id.reg_pass).error = "Please enter a password."
            return false
        }
        else if (pass != rePass) {
            findViewById<EditText>(R.id.reg_reenter_pass).error = "Passwords do not match."
            return false
        }
        else return true
    }

    private fun checkUserExistsAndCreateUser(user: HashMap<String,Serializable>)
    {
        Data.db.collection(Data.USERS).get()
            .addOnSuccessListener { querySnapshot ->
                for(doc in querySnapshot.documents)
                {
                    Log.e("TAG",doc.id)
                    if(doc.id.equals(user[Data.USER_PHONE] as String))
                    {
                        reg_phone.error = "User Already Exists"
                        Toast.makeText(this,"User Already Exists",Toast.LENGTH_LONG).show()
                        return@addOnSuccessListener
                    }
                }
                createUser(user)
            }
    }

    private fun createUser(user: HashMap<String,Serializable>)
    {
        Data.db.collection("users").document(user[Data.USER_PHONE] as String).set(user)
            .addOnSuccessListener { void ->
                getSharedPreferences(Data.SHAREDPREF, Context.MODE_PRIVATE).edit().putString(Data.SAVEDUSER,user[Data.USER_PHONE] as String).commit()
                Toast.makeText(this,"Welcome",Toast.LENGTH_LONG).show()
                var i = Intent(this,MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            }
            .addOnFailureListener { exception -> Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_LONG).show() }
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
