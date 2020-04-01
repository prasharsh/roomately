package com.titans.roomatelyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.titans.roomatelyapp.DataModels.Category
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.RecyclerViewAdapters.CategoryListAdapter
import com.titans.roomatelyapp.RecyclerViewAdapters.ItemListAdapter
import kotlinx.android.synthetic.main.activity_items.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.titans.roomatelyapp.barcodeReader.BarcodeReaderActivity
import com.titans.roomatelyapp.dialogs.ProductDetailDialog
import com.titans.roomatelyapp.items.ItemsActivity
import com.titans.roomatelyapp.login.RegistrationActivity
import kotlinx.android.synthetic.main.activity_item_crud.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Items : AppCompatActivity()
{
    val BARCODE_READER_ACTIVITY_REQUEST = 1208

    lateinit var adapter: CategoryListAdapter
    var categories = ArrayList<Category>()
    var filteredCategories = ArrayList<Category>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        var backButton = findViewById<ImageButton>(R.id.backButton)
        var txtToolbarLabel = findViewById<TextView>(R.id.txtToolbarLabel)
        var searchItem = findViewById<SearchView>(R.id.searchItem)
        val navToAddItem = findViewById<FloatingActionButton>(R.id.addItemFloatingButtoon)
        backButton.setOnClickListener { _ -> onBackPressed() }
        txtToolbarLabel.text = Data.selectedGroup+" > Items"

//        getData()
        adapter = CategoryListAdapter(this, filteredCategories)

        var animation = AnimationUtils.loadLayoutAnimation(this,R.anim.layout_animation_fall_down)
        categoryList.layoutAnimation = animation
        categoryList.scheduleLayoutAnimation()

        categoryList.adapter = adapter
        categoryList.layoutManager = LinearLayoutManager(this)

        searchItem.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText.toString())
                return false
            }
        })

        navToAddItem.setOnClickListener{v ->
            startActivity(Intent(v.context, ItemsActivity::class.java))}

        barcodScannerFloatingButtoon.setOnClickListener { v ->
            val launchIntent: Intent = BarcodeReaderActivity.getLaunchIntent(this, true, false)
            startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST)
        }
    }

    fun getData()
    {
        categories.clear()
        filteredCategories.clear()
        if(Data.selectedGroup.equals("Self"))
        {

            Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("items")
                .get().addOnSuccessListener { querySnapshot ->
                    for(doc in querySnapshot.documents)
                    {
                        var cat = Category(doc.id)
                        Log.e("TAG", doc.data.toString())
                        for(s in doc.data!!.keys)
                        {
                            Log.e("TAG",s)
                            var map = doc.data!!.get(s) as HashMap<*, *>
                            cat.items.add(Item(
                                name = map.get("name").toString(),
                                desc = map.get("desc").toString(),
                                inStock = map.get("inStock") as Boolean,
                                barcodes = map.get("barcodes") as ArrayList<String>,
                                locations = map.get("locations") as String
                            ))
                        }
                        categories.add(cat)
                    }
                    filteredCategories.addAll(categories)
                    adapter.notifyDataSetChanged()
                }
        }
        else
        {
            Log.e("TAG",Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1])
            Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1]).collection("items")
                .get().addOnSuccessListener { querySnapshot ->
                    Log.e("TAG","Documents: "+querySnapshot.documents.size)
                    for(doc in querySnapshot.documents)
                    {
                        var cat = Category(doc.id)
                        Log.e("TAG", doc.data.toString())
                        for(s in doc.data!!.keys)
                        {
                            Log.e("TAG",s)
                            var map = doc.data!!.get(s) as HashMap<*, *>
                            cat.items.add(Item(
                                name = map.get("name").toString(),
                                desc = map.get("desc").toString(),
                                inStock = map.get("inStock") as Boolean,
                                barcodes = map.get("barcodes") as ArrayList<String>,
                                locations = map.get("location") as String
                            ))
                        }
                        categories.add(cat)
                    }
                    filteredCategories.addAll(categories)
                    adapter.notifyDataSetChanged()
                }
        }
        Log.e("TAG", categories.size.toString())
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode!=BARCODE_READER_ACTIVITY_REQUEST)
            return

        if(data==null)
            return
        var barcode = data?.getStringExtra(BarcodeReaderActivity.KEY_CAPTURED_RAW_BARCODE)

        for(cat in categories)
        {
            for(item in cat.items)
            {
                if(item.barcodes.contains(barcode))
                {
                    ProductDetailDialog(item = item,category = cat.title).show(supportFragmentManager,"Product Detail")
                    return
                }
            }
        }

        Toast.makeText(this,"Item not found",Toast.LENGTH_LONG).show()

    }

    fun filter(search: String) {
        val searchTerm = search.toLowerCase(Locale.getDefault())
        filteredCategories.clear()

        if (searchTerm.isEmpty()) {
            filteredCategories.addAll(categories)
        }
        else {
            for(ctgry in categories) {
                val category = Category(ctgry.title)
                category.items.addAll(ctgry.items.filter { item ->
                    item.name.toLowerCase(Locale.getDefault()).contains(searchTerm)
                })
                if (category.items.isNotEmpty()) {
                    filteredCategories.add(category)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    //    fun getData(): ArrayList<Category>
//    {
//        var list = ArrayList<Category>()
//        for(j in 1..3)
//        {
//            var items = ArrayList<Item>()
//            for(i in 1..10)
//            {
//                var item = Item("Item$i",i%2==0)
//                items.add(item)
//            }
//            list.add(Category("Category$j",items))
//        }
//        return list
//    }
}
 