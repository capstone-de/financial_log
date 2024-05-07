package com.example.financiallog

import java.util.Date

data class ResponseExpend (
    val ReExpend : List<DataEx>
)
{
    data class DataEx(
        val user : Int,
        val date : Date,
        val price : Int,
        val category: String,
        val banme : String,
        val with_whom: HashMap<String,String>,
        val satisfaction: Int
    )
}