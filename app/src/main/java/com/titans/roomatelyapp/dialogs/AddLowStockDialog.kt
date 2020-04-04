package com.titans.roomatelyapp.dialogs

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.titans.roomatelyapp.Data
import com.titans.roomatelyapp.DataModels.Category
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.R
import com.titans.roomatelyapp.RecyclerViewAdapters.InStockListAdapter
import com.titans.roomatelyapp.RecyclerViewAdapters.ItemListAdapter
import com.titans.roomatelyapp.RecyclerViewAdapters.LowStockListAdapter
import kotlinx.android.synthetic.main.drawer_header.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AddLowStockDialog: DialogFragment
{
    var ctx: Context
    var adapter: LowStockListAdapter

    lateinit var listItems: RecyclerView

    var inStockItems = ArrayList<LowStockListAdapter.LowItem>()
    lateinit var listItemsAdapter: InStockListAdapter

    constructor(ctx: Context, adapter: LowStockListAdapter) : super()
    {
        this.ctx = ctx
        this.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.dialog_add_low_stock,container,false)

        listItems = view.findViewById(R.id.listItems)

        getData()
        listItemsAdapter = InStockListAdapter(ctx,inStockItems,adapter)
        listItems.adapter = listItemsAdapter
        listItems.layoutManager = LinearLayoutManager(ctx)

        return view
    }

    fun getData()
    {
        inStockItems.clear()
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
                            if(i.inStock)
                                inStockItems.add(LowStockListAdapter.LowItem(i, cat))
                        }
                        listItemsAdapter.notifyDataSetChanged()
                    }
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

                            if(i.inStock)
                                inStockItems.add(LowStockListAdapter.LowItem(i, cat))
                        }
                        listItemsAdapter.notifyDataSetChanged()
                    }
                }
        }
        Log.e("TAG", inStockItems.size.toString())
    }

//    private fun checkValuesAndUpdate(): Boolean
//    {
//        var name = editName.text.toString()
//        var desc = editDesc.text.toString()
//        var barcodes = editBarcodes.text.toString()
//        var status = checkStatus.isChecked
//
//        if(name.equals(""))
//        {
//            editName.error = "Enter Name of Product"
//            return false
//        }
//        if(desc.equals(""))
//        {
//            editDesc.error = "Enter Description of Product"
//            return false
//        }
//
//        txtItemName.text = name
//        txtItemDesc.text = desc
//
//        if(status)
//        {
//            txtItemStatus.text = "Status: In Stock"
//            txtItemStatus.setTextColor(context!!.resources.getColor(R.color.green))
//        }
//        else
//        {
//            txtItemStatus.text = "Status: Low Stock"
//            txtItemStatus.setTextColor(context!!.resources.getColor(R.color.red))
//        }
//
//        var barcodeList = ArrayList<String>()
//
//        var b = "Barcodes:"
//
//        for(barcode in barcodes.split("\n"))
//        {
//            if(!barcode.trim().equals(""))
//            {
//                barcodeList.add(barcode.trim())
//                b+="\n"+barcode.trim()
//            }
//        }
//
//        txtBarcodes.text = b
//
//        var item = Item(
//            name = name,
//            desc = desc,
//            inStock = status,
//            barcodes = barcodeList
//        )
//
//        var update = hashMapOf(
//            name to item
//        )
//
//        if(Data.selectedGroup.equals("Self"))
//        {
//            if(!name.equals(this.item.name))
//            {
//
//                var delete = hashMapOf<String, Any>(
//                    this.item.name to FieldValue.delete()
//                )
//                Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("items")
//                    .document(adapter.category).update(delete)
//            }
//
//            Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("items")
//                .document(adapter.category).set(update, SetOptions.merge())
//                .addOnSuccessListener { void ->
//                    this.item.copy(item)
//                    this.adapter.notifyDataSetChanged()
//                }
//        }
//        else
//        {
//            if(!name.equals(this.item.name))
//            {
//
//                var delete = hashMapOf<String, Any>(
//                    this.item.name to FieldValue.delete()
//                )
//                Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1]).collection("items")
//                    .document(adapter.category).update(delete)
//            }
//
//            Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1]).collection("items")
//                .document(adapter.category).set(update, SetOptions.merge())
//                .addOnSuccessListener { void ->
//                    this.item.copy(item)
//                    this.adapter.notifyDataSetChanged()
//                }
//        }
//        switchVisibility(true)
//        return true
//
//    }
//
//    fun deleteItem()
//    {
//        adapter.items.remove(item)
//
//        if(Data.selectedGroup.equals("Self"))
//        {
//           val i = hashMapOf<String,Any>(
//               item.name to FieldValue.delete()
//           )
//
//            Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("items").document(adapter.category).update(i)
//                .addOnSuccessListener { dismiss() }
//        }
//        else
//        {
//
//            val i = hashMapOf<String,Any>(
//                item.name to FieldValue.delete()
//            )
//
//            Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1]).collection("items").document(adapter.category).update(i)
//                    .addOnSuccessListener { dismiss() }
//        }
//
//        adapter.notifyDataSetChanged()
//    }
//
//    fun setValues()
//    {
//        editName.setText(item.name)
//        editDesc.setText(item.desc)
//        checkStatus.isChecked = item.inStock
//
//        var b = ""
//
//        for(barcode in item.barcodes)
//        {
//            b+="\n"+barcode
//        }
//
//        editBarcodes.setText(b)
//    }
//
//    fun switchVisibility(visiblePrimary: Boolean)
//    {
//        var primary = View.GONE
//        var edit = View.VISIBLE
//        if(visiblePrimary)
//        {
//            primary = View.VISIBLE
//            edit = View.GONE
//        }
//
//        txtItemName.visibility = primary
//        txtItemDesc.visibility = primary
//        txtItemStatus.visibility = primary
//        txtBarcodes.visibility = primary
//        normalLinear.visibility = primary
//
//        editName.visibility = edit
//        editDesc.visibility = edit
//        editBarcodes.visibility = edit
//        checkStatus.visibility = edit
//        editLinear.visibility = edit
//    }
}