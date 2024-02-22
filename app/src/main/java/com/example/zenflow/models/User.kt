package com.example.zenflow.models

data class User(
    val userId:String?=null,
    val userName:String,
    val userEmail:String,
    val userPassword:String?=null
)