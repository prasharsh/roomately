package com.titans.roomatelyapp.DataModels

class Transaction
{
    var title: String
    var subTitle: String
    var date:String

    constructor(title: String, subTitle: String, date: String) {
        this.title = title
        this.subTitle = subTitle
        this.date = date
    }
}