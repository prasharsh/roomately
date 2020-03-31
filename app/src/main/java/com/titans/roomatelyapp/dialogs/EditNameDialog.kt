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
import com.titans.roomatelyapp.R

class EditNameDialog: DialogFragment
{
    var ctx: Context
    var txtView: TextView

    constructor(ctx: Context, txtView: TextView) : super() {
        this.ctx = ctx
        this.txtView = txtView
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.dialog_edit_name,container,false)

        view.minimumWidth = 900

        var changeNameButton = view.findViewById<Button>(R.id.changeNameButton)
        var inputNewName = view.findViewById<TextInputEditText>(R.id.inputNewName)


        changeNameButton.setOnClickListener { v ->
            var newName = inputNewName.text.toString().trim()

            if(newName.equals(""))
            {
                inputNewName.error = "Please Enter Valid Name"
            }
            else
            {
                val change = hashMapOf(
                    Data.USER_NAME to newName
                )
                Data.db.collection(Data.USERS).document(Data.currentUser.phone).set(change, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(ctx,"Name Changed",Toast.LENGTH_LONG).show()

                        txtView.text = newName
                        Data.currentUser.name = newName
                        dismiss()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(ctx,"Error Changing Name!! Please Try Again Later",Toast.LENGTH_LONG).show()
                    }
            }
        }

        return view;
    }
}