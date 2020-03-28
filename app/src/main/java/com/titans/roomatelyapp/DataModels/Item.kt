package com.titans.roomatelyapp.DataModels

class Item
{
    var name: String
    var inStock: Boolean

    constructor(name: String, inStock: Boolean)
    {
        this.name = name
        this.inStock = inStock
    }
}