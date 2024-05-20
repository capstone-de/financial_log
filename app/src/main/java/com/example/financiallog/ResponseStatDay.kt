package com.example.financiallog

data class ResponseStatDay(
    val totalIncome: Int,
    val totalExpenses: Int,
    val expenses: List<DayExpense>
){
    data class DayExpense(
        val category: String,
        val bname: String,
        val price: Int

    )
}