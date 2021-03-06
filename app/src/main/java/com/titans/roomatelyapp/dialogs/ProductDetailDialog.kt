package com.titans.roomatelyapp.dialogs

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.titans.roomatelyapp.Data
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.DataModels.Transaction
import com.titans.roomatelyapp.R
import com.titans.roomatelyapp.RecyclerViewAdapters.ItemListAdapter
import com.titans.roomatelyapp.barcodeReader.BarcodeReaderActivity
import com.titans.roomatelyapp.items.LocationActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductDetailDialog: DialogFragment
{

    val BARCODE_READER_ACTIVITY_REQUEST = 1208
    val LOCATION_ACTIVITY_REQUEST = 1308
    var item: Item
    var adapter: ItemListAdapter?
    var category: String?



    lateinit var txtItemName: TextView
    lateinit var txtItemDesc: TextView
    lateinit var txtItemStatus: TextView
    lateinit var txtBarcodes: TextView
    lateinit var txtLocation: TextView

    lateinit var lName: TextInputLayout
    lateinit var lDesc: TextInputLayout
    lateinit var lBarcodes: TextInputLayout
    lateinit var lLocation: TextInputLayout

    lateinit var editButton: ImageButton
    lateinit var deleteButton: ImageButton
    lateinit var cameraButton: ImageButton
    lateinit var locationButton: ImageButton
    lateinit var normalLinear: LinearLayout

    lateinit var editName: EditText
    lateinit var editDesc: EditText
    lateinit var editBarcodes: EditText
    lateinit var editLocation: EditText
    lateinit var checkStatus: CheckBox
    lateinit var saveButton: ImageButton
    lateinit var cancelButton: ImageButton
    lateinit var editLinear: LinearLayout

    constructor(item: Item, adapter: ItemListAdapter?=null, category: String? = null) : super() {
        this.item = item
        this.adapter = adapter
        this.category = category

        if(category==null)
            this.category = adapter?.category
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.dialog_item,container,false)

        txtItemName = view.findViewById(R.id.txtItemName)
        txtItemDesc = view.findViewById(R.id.txtItemDesc)
        txtItemStatus = view.findViewById(R.id.txtStatus)
        txtBarcodes = view.findViewById(R.id.txtBarcodes)
        txtLocation = view.findViewById(R.id.txtLocation)

        lName = view.findViewById(R.id.lName)
        lDesc = view.findViewById(R.id.lDesc)
        lBarcodes = view.findViewById(R.id.lBarcodes)
        lLocation = view.findViewById(R.id.lLocation)

        editButton = view.findViewById(R.id.editButton)
        deleteButton = view.findViewById(R.id.deletButton)
        cameraButton = view.findViewById(R.id.cameraButton)
        locationButton = view.findViewById(R.id.locationButton)
        normalLinear = view.findViewById(R.id.noramlLinear)

        editName = view.findViewById(R.id.editName)
        editDesc = view.findViewById(R.id.editDesc)
        editBarcodes = view.findViewById(R.id.editBarcodes)
        editLocation = view.findViewById(R.id.editLocation)
        checkStatus = view.findViewById(R.id.checkStatus)
        saveButton = view.findViewById(R.id.saveButton)
        cancelButton = view.findViewById(R.id.cancelButton)
        editLinear = view.findViewById(R.id.hiddenLinear)

        txtItemName.text = item.name

        txtItemDesc.text = item.desc

        if(item.inStock)
        {
            txtItemStatus.text = "Status: In Stock"
            txtItemStatus.setTextColor(context!!.resources.getColor(R.color.green))
        }
        else
        {
            txtItemStatus.text = "Status: Low Stock"
            txtItemStatus.setTextColor(context!!.resources.getColor(R.color.red))
        }

        var b = "Barcodes: "

        for(barcode in item.barcodes)
        {
            b+="\n"+barcode
        }

        txtBarcodes.text = b

        Log.e("TAG",item.locations)
        Log.e("TAG",item.locations.split(Data.CONCAT)[0])

        txtLocation.text = "Location: \n"+item.locations

        txtLocation.setOnClickListener { v ->
            if(!item.locations.equals(""))
            {
                val uri = "http://maps.google.co.in/maps?q=" + item.locations
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                requireContext().startActivity(intent)
            }
        }

        deleteButton.setOnClickListener { v -> deleteItem() }

        editButton.setOnClickListener { v ->
            setValues()
            switchVisibility(false)
        }

        saveButton.setOnClickListener { v -> checkValuesAndUpdate() }

        cancelButton.setOnClickListener { v -> switchVisibility(true) }

        cameraButton.setOnClickListener { v ->
            val launchIntent: Intent = BarcodeReaderActivity.getLaunchIntent(view.context, true, false)
            startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST)
        }

        locationButton.setOnClickListener { v ->
            val intent = Intent(requireContext(),LocationActivity::class.java)

            startActivityForResult(intent,LOCATION_ACTIVITY_REQUEST)
        }

        return view
    }

    private fun checkValuesAndUpdate(): Boolean
    {
        var name = editName.text.toString()
        var desc = editDesc.text.toString()
        var barcodes = editBarcodes.text.toString()
        var status = checkStatus.isChecked
        var location = editLocation.text.toString().trim()


        if(name.equals(""))
        {
            editName.error = "Enter Name of Product"
            return false
        }
        if(desc.equals(""))
        {
            editDesc.error = "Enter Description of Product"
            return false
        }

        txtItemName.text = name
        item.name = name

        txtItemDesc.text = desc
        item.desc = desc

        txtLocation.text = "Location:\n"+location
        item.locations = location

        if(status)
        {
            txtItemStatus.text = "Status: In Stock"
            txtItemStatus.setTextColor(context!!.resources.getColor(R.color.green))
        }
        else
        {
            txtItemStatus.text = "Status: Low Stock"
            txtItemStatus.setTextColor(context!!.resources.getColor(R.color.red))
        }
        item.inStock = status

        var barcodeList = ArrayList<String>()

        var b = "Barcodes:"

        for(barcode in barcodes.split("\n"))
        {
            if(!barcode.trim().equals(""))
            {
                barcodeList.add(barcode.trim())
                b+="\n"+barcode.trim()
            }
        }

        txtBarcodes.text = b
        item.barcodes = barcodeList


        var update = hashMapOf(
            name to item
        )

        if(Data.selectedGroup.equals("Self"))
        {
            if(!name.equals(this.item.name))
            {

                var delete = hashMapOf<String, Any>(
                    this.item.name to FieldValue.delete()
                )
                Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("items")
                    .document(category!!).update(delete)
            }

            Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("items")
                .document(category!!).set(update, SetOptions.merge())
                .addOnSuccessListener { void ->
                    if(adapter!=null)
                        this.adapter?.notifyDataSetChanged()
                }
        }
        else
        {
            if(!name.equals(this.item.name))
            {

                var delete = hashMapOf<String, Any>(
                    this.item.name to FieldValue.delete()
                )
                Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1]).collection("items")
                    .document(category!!).update(delete)
            }

            Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1]).collection("items")
                .document(category!!).set(update, SetOptions.merge())
                .addOnSuccessListener { void ->
                    if(adapter!=null)
                        this.adapter?.notifyDataSetChanged()
                }
        }
        switchVisibility(true)
        return true

    }

    fun deleteItem()
    {
        var alert = AlertDialog.Builder(requireContext())

        alert.setIcon(resources.getDrawable(R.drawable.low_stock))
        alert.setTitle("Alert")
        alert.setMessage("Do you want to delete this item ?")

        alert.setPositiveButton("Delete", {dialog, which ->
            deleteConfirmed()
        })

        alert.setNegativeButton("Cancel",{dialog, which -> })
        alert.show()
    }

    fun deleteConfirmed()
    {
        if(adapter!=null)
            adapter!!.items.remove(item)

        if(Data.selectedGroup.equals("Self"))
        {
            val i = hashMapOf<String,Any>(
                item.name to FieldValue.delete()
            )

            Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("items").document(category!!).update(i)
                .addOnSuccessListener {

                    var t = Transaction(
                        title = item.name+ " back in stock",
                        subTitle = "Marked By: "+Data.currentUser.name,
                        date = SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime())
                    )
                    Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("transactions")
                        .document(Data.getTimeStamp()).set(t)
                    dismiss()
                }
        }
        else
        {
            val i = hashMapOf<String,Any>(
                item.name to FieldValue.delete()
            )

            Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1]).collection("items").document(category!!).update(i)
                .addOnSuccessListener {
                    var t = Transaction(
                        title = item.name+ " back in stock",
                        subTitle = "Marked By: "+Data.currentUser.name,
                        date = SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime())
                    )
                    Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1])
                        .collection("transactions").document(Data.getTimeStamp()).set(t)

                    dismiss()
                }
        }

        if(adapter!=null)
            adapter?.notifyDataSetChanged()
    }

    fun setValues()
    {
        editName.setText(item.name)
        editDesc.setText(item.desc)
        checkStatus.isChecked = item.inStock
        editLocation.setText(item.locations)

        var b = ""

        for(barcode in item.barcodes)
        {
            if(item.barcodes.indexOf(barcode)==0)
                b+=barcode
            else
                b+="\n"+barcode
        }

        editBarcodes.setText(b)
    }

    fun switchVisibility(visiblePrimary: Boolean)
    {
        var primary = View.GONE
        var edit = View.VISIBLE
        if(visiblePrimary)
        {
            primary = View.VISIBLE
            edit = View.GONE
        }

        txtItemName.visibility = primary
        txtItemDesc.visibility = primary
        txtItemStatus.visibility = primary
        txtLocation.visibility = primary
        txtBarcodes.visibility = primary
        normalLinear.visibility = primary

        lName.visibility = edit
        lDesc.visibility = edit
        lBarcodes.visibility = edit
        lLocation.visibility = edit
        checkStatus.visibility = edit
        editLinear.visibility = edit
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if(data==null)
            return

        if(requestCode == LOCATION_ACTIVITY_REQUEST)
        {
            if (data == null)
                return

            var locationName = data?.getStringExtra("Name")
            var locationAddress = data?.getStringExtra("Address")

            item.locations = locationName+"-"+locationAddress
            var update = hashMapOf(
                item.name to item
            )
            if(Data.selectedGroup.equals("Self"))
            {

                Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("items")
                    .document(category!!).set(update, SetOptions.merge())
                    .addOnSuccessListener { void ->
//                        this.item.copy(item)
                    }
            }
            else
            {

                Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1]).collection("items")
                    .document(category!!).set(update, SetOptions.merge())
                    .addOnSuccessListener { void ->
//                        this.item.copy(item)
                    }
            }


            txtLocation.text = "Location: "+item.locations

            return
        }

        if(requestCode==BARCODE_READER_ACTIVITY_REQUEST)
        {

            var barcode = data?.getStringExtra(BarcodeReaderActivity.KEY_CAPTURED_RAW_BARCODE)

            if(item.barcodes.contains(barcode))
            {
                Toast.makeText(requireContext(),"Barcode Already Added",Toast.LENGTH_LONG).show()
                return
            }
            item.barcodes.add(barcode!!)

            var update = hashMapOf(
                item.name to item
            )
            if(Data.selectedGroup.equals("Self"))
            {

                Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("items")
                    .document(category!!).set(update, SetOptions.merge())
                    .addOnSuccessListener { void ->
//                        this.item.copy(item)
                        if(adapter!=null)
                            this.adapter?.notifyDataSetChanged()
                    }
            }
            else
            {

                Data.db.collection(Data.GROUPS).document(Data.currentUser.groups[Data.groups.indexOf(Data.selectedGroup)-1]).collection("items")
                    .document(category!!).set(update, SetOptions.merge())
                    .addOnSuccessListener { void ->
//                        this.item.copy(item)
                        if(adapter!=null)
                            this.adapter?.notifyDataSetChanged()
                    }
            }

            txtBarcodes.text=txtBarcodes.text.toString()+"\n"+barcode
        }
    }
}