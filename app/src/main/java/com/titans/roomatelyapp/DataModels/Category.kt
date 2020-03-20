package com.titans.roomatelyapp.DataModels


class Category
{
    lateinit var title:String
    var items: List<Item> = ArrayList<Item>()

    constructor(title: String, items: List<Item>)
    {
        this.title = title
        this.items = items
    }
}