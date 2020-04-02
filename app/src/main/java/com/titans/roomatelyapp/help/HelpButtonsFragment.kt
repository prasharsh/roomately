package com.titans.roomatelyapp.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.titans.roomatelyapp.R
import kotlinx.android.synthetic.main.actionbar.*

class HelpButtonsFragment: Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var v = inflater.inflate(R.layout.help_buttons,container,false)

        var txtAddItem = v.findViewById<TextView>(R.id.txtAddItem)
        var txtDeleteItem = v.findViewById<TextView>(R.id.txtDeleteItem)
        var txtDeleteCategory = v.findViewById<TextView>(R.id.txtDeleteCategory)
        var txtAddGroup = v.findViewById<TextView>(R.id.txtAddGroup)
        var txtAddGroupMemeber = v.findViewById<TextView>(R.id.txtAddGroupMembers)
        var txtAcceptInvitation = v.findViewById<TextView>(R.id.txtAcceptInvitations)
        var txtSelectActiveGroup = v.findViewById<TextView>(R.id.txtSelectActiveGroup)
        var txtRemoveItemFromLowStock = v.findViewById<TextView>(R.id.txtRemoveItemFromLowStock)
        var txtAddItemToLowStock = v.findViewById<TextView>(R.id.txtAddItemToLowStock)
        var txtEditName = v.findViewById<TextView>(R.id.txtEditName)
        var txtChangePassword = v.findViewById<TextView>(R.id.txtChangePassword)

        txtAddItem.setOnClickListener { v ->
            findNavController().navigate(R.id.goToAddItem)
        }

        txtDeleteItem.setOnClickListener { v ->
            findNavController().navigate(R.id.goToDeleteItem)
        }

        txtDeleteCategory.setOnClickListener { v ->
            findNavController().navigate(R.id.goToDeleteCategory)
        }

        txtAddGroup.setOnClickListener { v ->
            findNavController().navigate(R.id.goToAddGroup)
        }

        txtAddGroupMemeber.setOnClickListener { v ->
            findNavController().navigate(R.id.goToAddMember)
        }

        txtAcceptInvitation.setOnClickListener { v ->
            findNavController().navigate(R.id.goToAcceptInvitation)
        }

        txtSelectActiveGroup.setOnClickListener { v ->
            findNavController().navigate(R.id.goToSelectActiveGroup)
        }

        txtRemoveItemFromLowStock.setOnClickListener { v ->
            findNavController().navigate(R.id.gotToRemoveItemFromLowStock)
        }

        txtAddItemToLowStock.setOnClickListener { v ->
            findNavController().navigate(R.id.goToAddItemToLowStock)
        }

        txtEditName.setOnClickListener { v ->
            findNavController().navigate(R.id.goToEditName)
        }

        txtChangePassword.setOnClickListener { v ->
            findNavController().navigate(R.id.goToChangePassword)
        }

        v.findViewById<ImageButton>(R.id.backButton).setOnClickListener { v ->
            activity?.onBackPressed()
        }

        v.findViewById<TextView>(R.id.txtToolbarLabel).text="Help"

        return v;
    }
}