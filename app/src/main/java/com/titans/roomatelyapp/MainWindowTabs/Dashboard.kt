package com.titans.roomatelyapp.MainWindowTabs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.titans.roomatelyapp.*
import com.titans.roomatelyapp.DataModels.Category
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.barcodeReader.BarcodeReaderActivity
import com.titans.roomatelyapp.dialogs.ProductDetailDialog
import com.titans.roomatelyapp.items.ItemsActivity

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
        if(data==null)
            return

        var b = data?.getStringExtra(BarcodeReaderActivity.KEY_CAPTURED_RAW_BARCODE)

        if(Data.selectedGroup.equals("Self"))
        {

            Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("items")
                .get().addOnSuccessListener { querySnapshot ->
                    searchItem(querySnapshot,requireContext(),requireFragmentManager(),b)
                }
        }
        else
        {
            Log.e("TAG", Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1])
            Data.db.collection(Data.GROUPS).document(
                    Data.currentUser.groups[Data.groups.indexOf(
                        Data.selectedGroup)-1]).collection("items")
                .get().addOnSuccessListener { querySnapshot ->
                    searchItem(querySnapshot,requireContext(),requireFragmentManager(),b)
                    }
        }
    }

    fun searchItem(querySnapshot: QuerySnapshot,ctx: Context, fragmentManager: FragmentManager, b: String)
    {
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

                if(i.barcodes.contains(b))
                {
                    Log.e("TAG","OPENING PRODUCT")
                    ProductDetailDialog(item = i,category = cat.title).show(fragmentManager,"Product Detail")
                    return
                }
            }
        }

        createAlert(ctx, b)
    }

    fun createAlert(ctx: Context, b:String)
    {
        var alert = AlertDialog.Builder(ctx)

        alert.setTitle("Product Not Found")
        alert.setMessage("Would you like to add this product?")
        alert.setPositiveButton("Add", {
            dialog, which ->
            var i = Intent(ctx,ItemsActivity::class.java)

            i.putExtra("barcode",b)
            ctx.startActivity(i)
        })
        alert.setNegativeButton("Cancel",{dialog, which ->  })
        alert.show()
    }
}