package com.titans.roomatelyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.titans.roomatelyapp.DataModels.User

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

        updatePage(Data.currentUser)
        /* Extract profile object from Firebase. */
//        val user = FirebaseAuth.getInstance().currentUser
//        FirebaseFirestore.getInstance()
//            .collection("profiles")
//            .document(user?.uid.toString())
//            .addSnapshotListener { snap, e ->
//                val userProfile = snap?.toObject(Profile::class.java)
//                updatePage(userProfile)
//            }


        /* Logout, edit, and delete listeners. */
//        findViewById<Button>(R.id.logout).setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//            startActivity(Intent(backButton.context, LoginActivity::class.java))
//        }

//        findViewById<Button>(R.id.profile_edit).setOnClickListener {
//
//        }
//        findViewById<TextView>(R.id.delete_account).setOnClickListener {
//            FirebaseFirestore.getInstance()
//                .collection("profiles").document(user?.uid.toString()).delete()
//                .addOnSuccessListener { FirebaseAuth.getInstance().currentUser!!.delete()
//                    .addOnCompleteListener {
//                        startActivity(Intent(backButton.context, LoginActivity::class.java))
//                    }
//                }
//        }

    }

    private fun updatePage(user: User?) {
        val nameView = findViewById<TextView>(R.id.profile_name)
        val phoneView = findViewById<TextView>(R.id.profile_phone)

        nameView.text = user?.name
        phoneView.text = user?.phone
    }
}
