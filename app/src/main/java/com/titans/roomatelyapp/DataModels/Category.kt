package com.titans.roomatelyapp.DataModels


class Category
{
    lateinit var title:String
    var items= ArrayList<Item>()

    constructor(title: String)
    {
        this.title = title
    }
}