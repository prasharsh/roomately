package com.titans.roomatelyapp.DataModels

data class User(val name: String = "",
                val phone: String = "",
                var groups: ArrayList<String> = ArrayList()
)