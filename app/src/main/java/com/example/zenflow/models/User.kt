package com.example.zenflow.models

data class User(
    val userName:String,
    val userEmail:String?=null,
    val userPassword:String
)