package com.example.financiallog

import java.util.ArrayList
import java.util.Date

data class ResponseIncome (
    val income : ArrayList<DataIn>,
)
{
    data class DataIn(
//        val user : Int,
//        val date : Date,
        val price : Int,
        val category: String,
//        val memo: String
    )
}