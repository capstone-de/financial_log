package com.example.financiallog

import java.util.ArrayList
import java.util.Date

data class ResponseExpend (
    val expense : ArrayList<DataEx>
)
{
    data class DataEx(
        val price : Int,
        val category: String,
        val bname : String,
    )
}