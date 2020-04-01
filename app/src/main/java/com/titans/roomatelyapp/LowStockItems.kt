package com.titans.roomatelyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.titans.roomatelyapp.DataModels.Category
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.RecyclerViewAdapters.LowStockListAdapter
import com.titans.roomatelyapp.dialogs.AddLowStockDialog
import kotlinx.android.synthetic.main.activity_low_stock_items.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class LowStockItems : AppCompatActivity()
{

    var lowStockItems = ArrayList<LowStockListAdapter.LowItem>()
    var filteredLowStockItems = ArrayList<LowStockListAdapter.LowItem>()
    lateinit var adapter: LowStockListAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_low_stock_items)

        var backButton = findViewById<ImageButton>(R.id.backButton)
        var txtToolbarLabel = findViewById<TextView>(R.id.txtToolbarLabel)
        var searchLowStockItem = findViewById<SearchView>(R.id.searchLowStockItem)
        backButton.setOnClickListener { v -> onBackPressed() }
        txtToolbarLabel.text = Data.selectedGroup +" > Low Stock"

        adapter = LowStockListAdapter(this,filteredLowStockItems)
        lowStockItemsList.adapter = adapter
        lowStockItemsList.layoutManager = LinearLayoutManager(this)

        addLowStockItemFloatingButtoon.setOnClickListener { v ->
            var dialogAddLowStockDialog = AddLowStockDialog(this,adapter)
            dialogAddLowStockDialog.show(supportFragmentManager,"Add Items to Low Stock")
        }

        searchLowStockItem.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText.toString())
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    fun getData()
    {
        lowStockItems.clear()
        filteredLowStockItems.clear()
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
                            var i = Item(
                                name = map.get("name").toString(),
                                desc = map.get("desc").toString(),
                                inStock = map.get("inStock") as Boolean
                            )

                            cat.items.add(i)
                            if(!i.inStock)
                                lowStockItems.add(LowStockListAdapter.LowItem(i,cat))
                        }
                    }
                    filteredLowStockItems.addAll(lowStockItems)
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
                            var i = Item(
                                name = map.get("name").toString(),
                                desc = map.get("desc").toString(),
                                inStock = map.get("inStock") as Boolean
                            )

                            cat.items.add(i)

                            if(!i.inStock)
                                lowStockItems.add(LowStockListAdapter.LowItem(i,cat))
                        }
                    }
                    filteredLowStockItems.addAll(lowStockItems)
                    adapter.notifyDataSetChanged()
                }
        }
        Log.e("TAG", lowStockItems.size.toString())
    }

    fun filter(search: String) {
        val searchTerm = search.toLowerCase(Locale.getDefault())
        filteredLowStockItems.clear()

        if (searchTerm.isEmpty()) {
            filteredLowStockItems.addAll(lowStockItems)
        }
        else {
            filteredLowStockItems.addAll(lowStockItems.filter { lowItem ->
                lowItem.item.name.toLowerCase(Locale.getDefault()).contains(searchTerm)
            })
        }

        adapter.notifyDataSetChanged()
    }
}
