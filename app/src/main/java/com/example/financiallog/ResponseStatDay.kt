package com.example.financiallog

data class ResponseStatDay(
    val total_income: Int,
    val total_expenses: Int,
    val expenses: List<DayExpense>
){
    data class DayExpense(
        val category: String,
        val bname: String,
        val price: Int
    )
}