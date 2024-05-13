package com.example.financiallog

data class ResponseCalender (
    val diary :ArrayList<DataDiary>,
    val expense :ArrayList<DataExpense>,
    val income:ArrayList<DataIncome>

){
    data class DataDiary(
        val data : String

    )
    data class DataExpense(
        val data: String
    )
    data class DataIncome(
        val data:String
    )
}
