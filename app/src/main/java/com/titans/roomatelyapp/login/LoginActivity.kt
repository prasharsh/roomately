package com.titans.roomatelyapp.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
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

        navToReg.setOnClickListener{v ->
            startActivity(Intent(v.context, RegistrationActivity::class.java))
        }

        /* Hides soft keyboard when focus changes from bill input editText. */
        emailField.setOnFocusChangeListener { view, b ->
            if (!b) hideKeyBoard(view)
        }

        emailLoginButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passField.text.toString()
            print(email.plus(password).plus(" heynow\n"))
            authUser.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = authUser.currentUser
                        Toast.makeText(baseContext, "Wild, another success.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }



    /**
     * @param view view containing the keyboard.
     * InputMethodManager is implemented to hide keyboard.
     */
    private fun hideKeyBoard(view: View) {
        val inputManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
