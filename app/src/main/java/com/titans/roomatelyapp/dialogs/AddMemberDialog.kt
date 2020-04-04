package com.titans.roomatelyapp.dialogs

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.SetOptions
import com.titans.roomatelyapp.Data
import com.titans.roomatelyapp.DataModels.Invitation
import com.titans.roomatelyapp.R

class AddMemberDialog: DialogFragment
{
    var group:String
    var ctx: Context

    constructor(group: String, ctx: Context) : super()
    {
        this.group = group
        this.ctx = ctx
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.dialog_add_member,container,false)

        view.minimumWidth = 900

        var addButton = view.findViewById<Button>(R.id.addMemberButton)
        var inputPhone = view.findViewById<TextInputEditText>(R.id.inputPhone)


        addButton.setOnClickListener { v ->
            var phone = inputPhone.text.toString().trim()

            if(phone.equals(""))
            {
                inputPhone.error = "Please Enter Phone Number"
            }
            else
            {
                var users = Data.db.collection(Data.USERS)

                users.get()
                    .addOnSuccessListener { querySnapshot ->
                        for(doc in querySnapshot.documents)
                        {
                            if(doc.id.equals(phone))
                            {
                                var list = doc["invitations"]

                                var groups = doc["groups"]

                                if(groups!=null)
                                {
                                    if((groups as ArrayList<String>).contains(group))
                                    {
                                        inputPhone.error = "User is already in group"
                                        return@addOnSuccessListener
                                    }
                                }

                                var invitations = ArrayList<Invitation>()

                                if(list!=null) {
                                    for (invitation in (list as ArrayList<HashMap<String,String>>))
                                    {
                                        invitations.add(Invitation(group = invitation["group"]!!,phone = invitation["phone"]!!))
                                        if(invitation["group"].equals(group))
                                        {
                                            inputPhone.error = "Invitation already sent"
                                            return@addOnSuccessListener
                                        }
                                    }
                                }
                                invitations.add(Invitation(group = group,phone = Data.currentUser.phone))

                                var invitation = hashMapOf(
                                    "invitations" to invitations
                                )
                                doc.reference.set(invitation, SetOptions.merge())
                                    .addOnSuccessListener { void -> Toast.makeText(ctx,"Invitation Sent",Toast.LENGTH_LONG).show()
                                    Log.e("TAG","Invitation Sent")
                                    }
                                    .addOnFailureListener{exception ->
                                        Log.e("TAG","Error Sending Invitation")
                                        Toast.makeText(ctx,"Error Sending Invitation",Toast.LENGTH_LONG).show()}
                                dismiss()
                            }
                        }

                        inputPhone.error = "User Does Not Exists"

                    }
                    .addOnFailureListener { exception -> Log.e("TAG","Error Getting Users") }
            }
        }

        return view;
    }
}