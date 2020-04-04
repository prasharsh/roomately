package com.titans.roomatelyapp.DataModels

data class User(var name: String = "",
                val pass: String = "",
                val phone: String = "",
                var groups: ArrayList<String> = ArrayList()
)