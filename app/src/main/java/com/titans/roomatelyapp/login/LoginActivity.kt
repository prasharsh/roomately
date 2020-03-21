package com.titans.roomatelyapp.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.titans.roomatelyapp.MainActivity
import com.titans.roomatelyapp.R

class LoginActivity : AppCompatActivity()
{
    private val TAG = "RegistrationActivity"
    private val authUser = FirebaseAuth.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val phoneLoginButton = findViewById<Button>(R.id.phone_login_button)
        val emailLoginButton = findViewById<Button>(R.id.login_email_button)
        val navToReg = findViewById<TextView>(R.id.registrationLink)
        val emailField = findViewById<EditText>(R.id.login_email_field)
        val passField = findViewById<EditText>(R.id.login_pass_field)

        /* Navigate to registration. */
        navToReg.setOnClickListener{v ->
            startActivity(Intent(v.context, RegistrationActivity::class.java))
        }

        /* Process login attempt. */
        emailLoginButton.setOnClickListener {
            if (emailField.text.isEmpty()) emailField.error = "Please enter an email address."
            else if (passField.text.isEmpty()) passField.error = "Please enter a password"
            else {
                val email = emailField.text.toString()
                val password = passField.text.toString()
                authUser.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = authUser.currentUser
                            startActivity(Intent(emailField.context, MainActivity::class.java))
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
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
