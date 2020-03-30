package com.titans.roomatelyapp.DataModels

class Invitation
{
    var group:String
    var phone: String

    constructor(group: String, phone: String) {
        this.group = group
        this.phone = phone
    }
}