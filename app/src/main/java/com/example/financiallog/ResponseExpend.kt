package com.example.financiallog

import java.util.ArrayList
import java.util.Date

data class ResponseExpend (
    val expense : ArrayList<DataEx>
)
{
    data class DataEx(
//        val user : Int,
//        val date : Date,
        val price : Int,
        val category: String,
        val bname : String,
//        val with_whom: HashMap<String,String>,
//        val satisfaction: Int
    )
}