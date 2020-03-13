package com.titans.roomatelyapp.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
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
        val reenterPass = findViewById<EditText>(R.id.reg_reenter_pass).text.toString()
        val phoneRegButton = findViewById<Button>(R.id.reg_phone_button)
        val emailRegButton = findViewById<Button>(R.id.reg_email_button)
                phoneRegButton.setOnClickListener {

        }

        emailRegButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.reg_first_name).text.toString()
            val email = findViewById<EditText>(R.id.reg_email).text.toString()
            val password = findViewById<EditText>(R.id.reg_pass).text.toString()

            authUser.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        Log.d(TAG, "createUserWithEmail:success")
                        val user = authUser.currentUser
                        val profile = Profile(user?.uid.toString(), name, email, password, null)

                        addProfile(profile)
                        Toast.makeText(baseContext, "SUCCESS KEEP WINNING.",
                            Toast.LENGTH_SHORT).show()
                        startActivity(Intent(emailRegButton.context, MainActivity::class.java))
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun addProfile(p: Profile) {
        val ref = FirebaseFirestore.getInstance().collection("profiles").document(p.authId)
        ref.set(p)
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
