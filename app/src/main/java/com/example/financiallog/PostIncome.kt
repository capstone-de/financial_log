package com.example.financiallog

import java.util.Date

data class PostIncome(
    val user : Int,
    val date : Date,
    val price : Int,
    val category: String,
    val memo: String

)
