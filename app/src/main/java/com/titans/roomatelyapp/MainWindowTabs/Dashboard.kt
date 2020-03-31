package com.titans.roomatelyapp.MainWindowTabs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.titans.roomatelyapp.*
import com.titans.roomatelyapp.DataModels.Category
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.barcodeReader.BarcodeReaderActivity
import com.titans.roomatelyapp.dialogs.ProductDetailDialog

class Dashboard: Fragment()
{
    private val BARCODE_READER_ACTIVITY_REQUEST = 1208

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        var view = inflater.inflate(R.layout.tab_dashboard,container,false)

        var cardItems = view.findViewById<CardView>(R.id.cardItems)
        var cardLowStock = view.findViewById<CardView>(R.id.cardLowStock)
        var cardTransaction = view.findViewById<CardView>(R.id.cardTransaction)
        var cardBarcodeScanner = view.findViewById<CardView>(R.id.cardBarcodeScanner)

        cardItems.setOnClickListener{v ->
            startActivity(Intent(view.context, Items::class.java))
        }

        cardLowStock.setOnClickListener{v ->
            startActivity(Intent(view.context, LowStockItems::class.java))
        }

        cardTransaction.setOnClickListener{v ->
            startActivity(Intent(view.context, Transactions::class.java))
        }

        cardBarcodeScanner.setOnClickListener {
            val launchIntent: Intent = BarcodeReaderActivity.getLaunchIntent(view.context, true, false)
            startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if(requestCode!=BARCODE_READER_ACTIVITY_REQUEST)
            return

        if(Data.selectedGroup.equals("Self"))
        {

            Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("items")
                .get().addOnSuccessListener { querySnapshot ->
                    for(doc in querySnapshot.documents)
                    {
                        var cat = Category(doc.id)
                        for(s in doc.data!!.keys)
                        {
                            Log.e("TAG",s)
                            var map = doc.data!!.get(s) as HashMap<*, *>
                            var i = Item(
                                name = map.get("name").toString(),
                                desc = map.get("desc").toString(),
                                inStock = map.get("inStock") as Boolean,
                                barcodes = map.get("barcodes") as ArrayList<String>
                            )


                            if(i.barcodes.contains(data?.getStringExtra(BarcodeReaderActivity.KEY_CAPTURED_RAW_BARCODE)))
                            {
                                Log.e("TAG","OPENING PRODUCT")
                                ProductDetailDialog(item = i,category = cat.title).show(requireFragmentManager(),"Product Detail")
                                return@addOnSuccessListener
                            }

                        }
                    }
                }
        }
        else
        {
            Log.e("TAG", Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1])
            Data.db.collection(Data.GROUPS).document(
                    Data.currentUser.groups[Data.groups.indexOf(
                        Data.selectedGroup)-1]).collection("items")
                .get().addOnSuccessListener { querySnapshot ->
                    Log.e("TAG","Documents: "+querySnapshot.documents.size)
                    for(doc in querySnapshot.documents)
                    {
                        var cat = Category(doc.id)
                        Log.e("TAG", doc.data.toString())
                        for(s in doc.data!!.keys) {
                            Log.e("TAG", s)
                            var map = doc.data!!.get(s) as HashMap<*, *>
                            var i = Item(
                                name = map.get("name").toString(),
                                desc = map.get("desc").toString(),
                                inStock = map.get("inStock") as Boolean,
                                barcodes = map.get("barcodes") as ArrayList<String>
                            )

                            if(i.barcodes.contains(data?.getStringExtra(BarcodeReaderActivity.KEY_CAPTURED_RAW_BARCODE)))
                            {
                                ProductDetailDialog(item = i,category = cat.title).show(requireFragmentManager(),"Product Detail")
                                return@addOnSuccessListener
                            }
                        }
                    }
                }
        }
    }
}