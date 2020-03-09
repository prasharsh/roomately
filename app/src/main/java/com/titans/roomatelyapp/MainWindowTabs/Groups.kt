package com.titans.roomatelyapp.MainWindowTabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.titans.roomatelyapp.R

class Groups:Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        var view = inflater.inflate(R.layout.tab_groups,container,false)

        return view;
    }
}