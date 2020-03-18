package com.titans.roomatelyapp.model

data class Profile(val authId: String,
                   val name: String?,
                   val email: String?,
                   val password: String?,
                   val phone: String?) {}