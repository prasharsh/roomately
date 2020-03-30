package com.titans.roomatelyapp.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.titans.roomatelyapp.Data
import com.titans.roomatelyapp.R

class CreateGroupDialog: DialogFragment
{
    constructor() : super()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.dialog_create_group,container,false)

        view.minimumWidth = 900

        var createButton = view.findViewById<Button>(R.id.createGroupButton)
        var inputGroup = view.findViewById<TextInputEditText>(R.id.inputGroupName)


        createButton.setOnClickListener { v ->
            var groupName = inputGroup.text.toString().trim()

            if(groupName.equals(""))
            {
                inputGroup.error = "Please Enter Group Name"
            }
            else
            {
                var groups = Data.db.collection(Data.GROUPS)

                groups.get()
                    .addOnSuccessListener { querySnapshot ->
                        for(doc in querySnapshot.documents)
                        {
                            if(doc.id.toLowerCase().equals(Data.currentUser.phone+Data.CONCAT+groupName.toLowerCase()))
                            {
                                inputGroup.error = "Group Already Exists"
                                return@addOnSuccessListener
                            }
                        }

                        var members = hashMapOf(
                            "members" to arrayListOf(Data.currentUser.phone)
                        )

                        groups.document(Data.currentUser.phone+Data.CONCAT+groupName.toLowerCase()).set(members)
                            .addOnSuccessListener { void ->
                                Data.currentUser.groups.add(Data.currentUser.phone+Data.CONCAT+groupName)

                                if(Data.groupListAdapter!=null)
                                    Data.groupListAdapter?.notifyDataSetChanged()


                                Data.db.collection("users").document(Data.currentUser.phone).update("groups",Data.currentUser.groups)
                                    .addOnSuccessListener { void ->

                                        Data.groups.add(groupName)
                                        dismiss()
                                    }
                            }

                    }
            }
        }

        return view;
    }
}