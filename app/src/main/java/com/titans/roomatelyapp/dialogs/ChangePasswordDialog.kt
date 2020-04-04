package com.titans.roomatelyapp.dialogs

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.SetOptions
import com.titans.roomatelyapp.Data
import com.titans.roomatelyapp.DataModels.Invitation
import com.titans.roomatelyapp.MainActivity
import com.titans.roomatelyapp.R

class ChangePasswordDialog: DialogFragment
{
    var ctx: MainActivity


    lateinit var changePasswordButton: Button
    lateinit var editCurrentPassword: TextInputEditText
    lateinit var editNewPassword: TextInputEditText
    lateinit var editReNewPassword: TextInputEditText

    constructor(ctx: MainActivity) : super() {
        this.ctx = ctx
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.dialog_change_password,container,false)

        view.minimumWidth = 900

        changePasswordButton = view.findViewById<Button>(R.id.changePasswordButton)
        editCurrentPassword = view.findViewById<TextInputEditText>(R.id.editCurrentPassword)
        editNewPassword = view.findViewById<TextInputEditText>(R.id.editNewPassword)
        editReNewPassword = view.findViewById<TextInputEditText>(R.id.editReNewPassword)


        changePasswordButton.setOnClickListener { v ->
            var currentPassword = editCurrentPassword.text.toString()
            var newPassword = editNewPassword.text.toString()
            var reNewPassword = editReNewPassword.text.toString()

            if(checkPassword(currentPassword, newPassword, reNewPassword))
            {
                val update = hashMapOf(
                    Data.USER_PASS to newPassword
                )
                Data.db.collection(Data.USERS).document(Data.currentUser.phone).set(update, SetOptions.merge())
                    .addOnSuccessListener {
                        dismiss()
                        Toast.makeText(ctx,"Password Changed Successfully",Toast.LENGTH_LONG).show()
                        (ctx).logOut()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(ctx,"Can't Change Password!! Please Try Again Later",Toast.LENGTH_LONG).show()
                        dismiss()
                    }
            }
        }

        return view;
    }

    private fun checkPassword(currentPass: String, pass: String, rePass: String): Boolean {

        if(!currentPass.equals(Data.currentUser.pass))
        {
            editCurrentPassword.error = "Password is Incorrect"
            return false
        }
        //check password constrains
        if(!pass.matches(Regex("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}\$")))
        {
            editNewPassword.error = "Password Does Satisfy Conditions"
            return false
        }
        else if (pass != rePass) {
            editReNewPassword.error = "Passwords do not match."
            return false
        }
        else return true
    }
}