package com.titans.roomatelyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.DataModels.Transaction
import com.titans.roomatelyapp.RecyclerViewAdapters.TransactionsAdapter
import kotlinx.android.synthetic.main.actionbar.*
import kotlinx.android.synthetic.main.activity_transactions.*
import java.util.*
import kotlin.collections.ArrayList

class Transactions : AppCompatActivity()
{

    var transactions = ArrayList<Transaction>()
    var filteredTransactions = ArrayList<Transaction>()
    lateinit var adapter: TransactionsAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        var searchItem = findViewById<SearchView>(R.id.searchItem)
        backButton.setOnClickListener { v ->
            onBackPressed()
        }

        txtToolbarLabel.text = Data.selectedGroup+" > Transactions"

        adapter = TransactionsAdapter(this, filteredTransactions)

        transactionList.adapter = adapter
        transactionList.layoutManager = LinearLayoutManager(this)

        searchItem.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText.toString())
                return false
            }
        })
    }


    override fun onResume() {
        super.onResume()

        transactions.clear()
        filteredTransactions.clear()
        if(Data.selectedGroup.equals("Self"))
        {
            Data.db.collection(Data.USERS).document(Data.currentUser.phone).collection("transactions").get()
                .addOnSuccessListener { querySnapshot ->
                    for(doc in querySnapshot.documents)
                    {
                        var t = Transaction(
                            title = doc.getString("title")!!,
                            subTitle = doc.getString("subTitle")!!,
                            date = doc.getString("date")!!
                        )

                        transactions.add(t)
                    }
                    filteredTransactions.addAll(transactions)
                    adapter.notifyDataSetChanged()

                }
                .addOnFailureListener { exception ->
                    Log.e("TAG","Error Reading Transaction")
                }
        }
        else
        {
            Data.db.collection(Data.GROUPS).document(
                    Data.currentUser.groups[Data.groups.indexOf(
                        Data.selectedGroup
                    )-1]).collection("transactions").get()
                .addOnSuccessListener { querySnapshot ->
                    for(doc in querySnapshot.documents)
                    {
                        var t = Transaction(
                            title = doc.getString("title")!!,
                            subTitle = doc.getString("subTitle")!!,
                            date = doc.getString("date")!!
                        )

                        transactions.add(t)
                    }
                    filteredTransactions.addAll(transactions)
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.e("TAG","Error Reading Transaction")
                }
        }
    }

    fun filter(search: String) {
        val searchTerm = search.toLowerCase(Locale.getDefault())
        filteredTransactions.clear()

        if (searchTerm.isEmpty()) {
            filteredTransactions.addAll(transactions)
        }
        else {
            filteredTransactions.addAll(transactions.filter { item ->
                item.title.toLowerCase(Locale.getDefault()).contains(searchTerm)
            })
        }

        adapter.notifyDataSetChanged()
    }

//    fun getData():ArrayList<Transaction>
//    {
//        var t1 = Transaction(Item("Item1",true),"User1","2020-01-01",true)
//        var t2 = Transaction(Item("Item2",true),"User2","2020-05-01",false)
//        var t3 = Transaction(Item("Item3",true),"User3","2020-01-01",true)
//        var t4 = Transaction(Item("Item4",true),"User4","2020-08-16",false)
//
//        var t = ArrayList<Transaction>()
//        t.add(t1)
//        t.add(t2)
//        t.add(t3)
//        t.add(t4)
//
//        return t
//    }
}
