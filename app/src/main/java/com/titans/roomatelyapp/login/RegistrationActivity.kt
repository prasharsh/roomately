package com.titans.roomatelyapp.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.titans.roomatelyapp.MainActivity
import com.titans.roomatelyapp.R
import com.titans.roomatelyapp.model.Profile


class RegistrationActivity : AppCompatActivity()
{
    private val TAG = "RegistrationActivity"
    private val authUser = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val nameField = findViewById<EditText>(R.id.reg_first_name)
        val emailField = findViewById<EditText>(R.id.reg_email)
        val passField = findViewById<EditText>(R.id.reg_pass)
        val reenterPassField = findViewById<EditText>(R.id.reg_reenter_pass)
        val phoneRegButton = findViewById<Button>(R.id.reg_phone_button)
        val emailRegButton = findViewById<Button>(R.id.reg_email_button)

        /* Hide keyboard whenever user selects screen. */
        nameField.setOnFocusChangeListener { view, b ->
            if (!b) hideKeyBoard(view)
        }
        emailField.setOnFocusChangeListener { view, b ->
            if (!b) hideKeyBoard(view)
        }
        passField.setOnFocusChangeListener { view, b ->
            if (!b) hideKeyBoard(view)
        }
        reenterPassField.setOnFocusChangeListener { view, b ->
            if (!b) hideKeyBoard(view)
        }

        /* Navigate back to login. */
        val navToLogin = findViewById<TextView>(R.id.login_link)
        navToLogin.setOnClickListener{v ->
            startActivity(Intent(v.context, LoginActivity::class.java))
        }

        /* TODO: Extract phone number and register. */
        phoneRegButton.setOnClickListener {

        }

        /* Code to register with email. */
        emailRegButton.setOnClickListener {
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val password = passField.text.toString()
            val reenterPass = reenterPassField.text.toString()

            /* Validate user input before processing. */
            if (checkEmail(email) && checkPassword(password, reenterPass)) {
                authUser.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful)
                        {
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = authUser.currentUser
                            val profile = Profile(user?.uid.toString(), name, email, password, null)
                            addProfile(profile)
                            startActivity(Intent(emailRegButton.context, MainActivity::class.java))
                        } else
                        {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    /**
     * @param email string pulled from email field.
     * @return Boolean indicating pass or fail.
     */
    private fun checkEmail(email: String): Boolean {
        /*if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
            findViewById<EditText>(R.id.reg_email).error = "Please enter a valid email address."
            return false
        }*/
        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            findViewById<EditText>(R.id.reg_email).error = "Please enter a valid email address."
            return false
        }
        else return true
    }

    /**
     * @param pass string pulled from password field.
     * @param rePass re-entered user password.
     * @return Boolean indicating pass or fail.
     */
    private fun checkPassword(pass: String, rePass: String): Boolean {
        if (pass.isEmpty()) {
            findViewById<EditText>(R.id.reg_pass).error = "Please enter a password."
            return false
        }
        else if (pass.length < 6) {
            findViewById<EditText>(R.id.reg_pass).error = "Please enter a password with more than six characters."
            return false
        }
        else if (pass != rePass) {
            findViewById<EditText>(R.id.reg_reenter_pass).error = "Passwords do not match."
            return false
        }
        else return true
    }

    private fun addProfile(p: Profile) {
        val ref = FirebaseFirestore.getInstance().collection("profiles").document(p.authId)
        ref.set(p)
    }

    /**
     * @param view view holding the keyboard.
     * InputMethodManager is implemented to hide keyboard.
     */
    private fun hideKeyBoard(view: View) {
        val inputManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
