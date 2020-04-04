package com.titans.roomatelyapp.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.titans.roomatelyapp.R

class AcceptInvitationsFragment: Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.help_accept_invitation,container,false)

        v.findViewById<ImageButton>(R.id.backButton).setOnClickListener { v ->
            activity?.onBackPressed()
        }

        v.findViewById<TextView>(R.id.txtToolbarLabel).text="Help"
        return v;
    }
}