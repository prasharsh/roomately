package com.titans.roomatelyapp.DataModels

class Transaction
{

    companion object
    {
        val IN_STOCK = true
        val LOW_STOCK = false
    }
    var prodcut: Item
    var user: String
    var date:String
    var action: Boolean

    constructor(prodcut: Item, user: String, date: String, action: Boolean)
    {
        this.prodcut = prodcut
        this.user = user
        this.date = date
        this.action = action
    }
}