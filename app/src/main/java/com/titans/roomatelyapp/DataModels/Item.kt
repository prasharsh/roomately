package com.titans.roomatelyapp.DataModels

class Item
{
    var name = ""
    var desc = ""
    var inStock = false
    lateinit var barcodes:ArrayList<String>
    lateinit var locations: ArrayList<String>

    constructor(
        name: String="",
        desc: String="",
        inStock: Boolean,
        barcodes:ArrayList<String> =  ArrayList<String>(),
        locations: ArrayList<String> = ArrayList<String>()
    ) {
        this.name = name
        this.desc = desc
        this.inStock = inStock
        this.barcodes = barcodes
        this.locations = locations
    }

    fun copy(item: Item)
    {
        this.name = item.name
        this.desc = item.desc
        this.inStock = item.inStock
        this.barcodes = item.barcodes
        this.locations = item.locations
    }
}