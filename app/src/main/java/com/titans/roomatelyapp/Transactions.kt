package com.titans.roomatelyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.titans.roomatelyapp.DataModels.Item
import com.titans.roomatelyapp.DataModels.Transaction
import com.titans.roomatelyapp.RecyclerViewAdapters.TransactionsAdapter
import kotlinx.android.synthetic.main.activity_transactions.*

class Transactions : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        var adapter = TransactionsAdapter(this,getData())

        transactionList.adapter = adapter
        transactionList.layoutManager = LinearLayoutManager(this)

    }

    fun getData():ArrayList<Transaction>
    {
        var t1 = Transaction(Item("Item1",true),"User1","2020-01-01",true)
        var t2 = Transaction(Item("Item2",true),"User2","2020-05-01",false)
        var t3 = Transaction(Item("Item3",true),"User3","2020-01-01",true)
        var t4 = Transaction(Item("Item4",true),"User4","2020-08-16",false)

        var t = ArrayList<Transaction>()
        t.add(t1)
        t.add(t2)
        t.add(t3)
        t.add(t4)

        return t
    }
}
