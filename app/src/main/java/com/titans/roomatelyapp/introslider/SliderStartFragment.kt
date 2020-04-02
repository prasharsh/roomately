package com.titans.roomatelyapp.introslider

import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.titans.roomatelyapp.R
import kotlinx.android.synthetic.main.fragment_slider_start.*

/**
 * A simple [Fragment] subclass.
 */
class SliderStartFragment : Fragment() {
    private var introTextContent : String = ""
    private var introImageInt : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slider_start, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        introTextFragment.text = introTextContent
        introImage.setImageResource(introImageInt)
    }

    fun setIntroText(intro_Text : String){
        introTextContent = intro_Text
    }

    fun setIntroImage(image : Int){
        introImageInt = image
    }

}
