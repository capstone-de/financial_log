package com.example.financiallog

import java.util.Date

data class PostExpend(
    val user : Int,
    val date : Date,
    val price : Int,
    val category: String,
    val banme : String,
    val with_whom: ArrayList<String>,
    val satisfaction: Int,
)


