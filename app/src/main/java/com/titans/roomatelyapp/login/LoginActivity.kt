package com.titans.roomatelyapp.login

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.titans.roomatelyapp.Data
import com.titans.roomatelyapp.DataModels.User
import com.titans.roomatelyapp.MainActivity
import com.titans.roomatelyapp.R

class LoginActivity : AppCompatActivity()
{
    private val TAG = "RegistrationActivity"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val phoneLoginButton = findViewById<Button>(R.id.loginButton)
        val navToReg = findViewById<TextView>(R.id.registrationLink)
        val phoneField = findViewById<EditText>(R.id.inputPhoneNumber)
        val passField = findViewById<EditText>(R.id.inputPassword)

        /* Navigate to registration. */
        navToReg.setOnClickListener{v ->
            startActivity(Intent(v.context, RegistrationActivity::class.java))
        }

        /* Process login attempt. */
        phoneLoginButton.setOnClickListener {
            if (phoneField.text.isEmpty()) phoneField.error = "Please Enter Phone Number"
            else if (passField.text.isEmpty()) passField.error = "Please Enter Password"
            else
            {
                val phone = phoneField.text.toString().trim()
                val password = passField.text.toString()

                Data.db.collection(Data.USERS).document(phone).get()
                    .addOnSuccessListener { documentSnapshot ->
                        if(password.equals(documentSnapshot.getString(Data.USER_PASS)))
                        {
                            Data.currentUser = User(
                                name = documentSnapshot.getString(Data.USER_NAME)!!,
                                phone = phone,
                                groups = documentSnapshot.get(Data.USER_GROUPS) as ArrayList<String>
                            )

                            var editor = getSharedPreferences(Data.SHAREDPREF,Context.MODE_PRIVATE).edit()
                            editor.putString(Data.SAVEDUSER,phone)
                            editor.commit()

                            var i = Intent(this,MainActivity::class.java)
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(i)
                        }
                        else
                        {
                            passField.error = "Invalid Password"
                            phoneField.error = "Invalid Username"
                        }
                    }
            }
        }
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
