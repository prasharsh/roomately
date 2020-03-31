package com.titans.roomatelyapp.items

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.titans.roomatelyapp.R
import java.util.*


class LocationActivity : AppCompatActivity(){

    private var productName: String? =null
    private var productDesc: String? =null
    private var productCategory: String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_location)
        Places.initialize(applicationContext, getString(R.string.places_api))
        var intent = intent
        productName = intent.getStringExtra("productName")
        productDesc = intent.getStringExtra("productDesc")
        productCategory = intent.getStringExtra("productCategory")
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

        // Specify the types of place data to return.
        // Specify the types of place data to return.
        autocompleteFragment!!.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val intent = Intent(this@LocationActivity, ItemsActivity::class.java)
                intent.putExtra("Name", ""+place.name )
                intent.putExtra("Address", ""+place.address)

                intent.putExtra("productName", ""+productName)
                intent.putExtra("productCategory", ""+productCategory)
                intent.putExtra("productDesc", ""+productDesc)
                startActivity(intent)
                }

            override fun onError(status: Status) {
                Log.e("TAG","An error occurred: $status")
            }
        })
    }
}
