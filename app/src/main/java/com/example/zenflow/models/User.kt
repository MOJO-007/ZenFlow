package com.example.zenflow.models

import android.net.Uri

data class User(
    val userName:String,
    val userEmail:String?=null,
    val userPassword:String,
    val imageUri:Uri?=null
)