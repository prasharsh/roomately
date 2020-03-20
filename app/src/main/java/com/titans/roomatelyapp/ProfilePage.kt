package com.titans.roomatelyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.titans.roomatelyapp.login.LoginActivity
import com.titans.roomatelyapp.model.Profile

class ProfilePage : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        /* Initialize toolbar. */
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val txtToolbarLabel = findViewById<TextView>(R.id.txtToolbarLabel)
        backButton.setOnClickListener { onBackPressed() }
        txtToolbarLabel.text = getString(R.string.profile_header)

        /* Extract profile object from Firebase. */
        val user = FirebaseAuth.getInstance().currentUser
        FirebaseFirestore.getInstance()
            .collection("profiles")
            .document(user?.uid.toString())
            .addSnapshotListener { snap, e ->
                val userProfile = snap?.toObject(Profile::class.java)
                updatePage(userProfile)
            }

        /* Logout, edit, and delete listeners. */
        findViewById<Button>(R.id.logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(backButton.context, LoginActivity::class.java))
        }

        findViewById<Button>(R.id.profile_edit).setOnClickListener {

        }
        findViewById<TextView>(R.id.delete_account).setOnClickListener {
            FirebaseFirestore.getInstance()
                .collection("profiles").document(user?.uid.toString()).delete()
                .addOnSuccessListener { FirebaseAuth.getInstance().currentUser!!.delete()
                    .addOnCompleteListener {
                        startActivity(Intent(backButton.context, LoginActivity::class.java))
                    }
                }
        }

    }

    private fun updatePage(profile: Profile?) {
        val nameView = findViewById<TextView>(R.id.profile_name)
        val emailView = findViewById<TextView>(R.id.profile_email)
        val phoneView = findViewById<TextView>(R.id.profile_phone)

        nameView.text = profile?.name
        emailView.text = profile?.email
        phoneView.text = profile?.phone
    }
}
