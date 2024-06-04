package com.example.financiallog

data class ResponseStatWeek (
    val mon: DayStatistics? = null,
    val tue: DayStatistics? = null,
    val wed: DayStatistics? = null,
    val thu: DayStatistics? = null,
    val fri: DayStatistics? = null,
    val sat: DayStatistics? = null,
    val sun: DayStatistics? = null,

    //val category: WeekCategory

    val category1: WeekCategory = WeekCategory(emptyList(), emptyList()) // 초기화된 변수
) {
    data class DayStatistics(
        val totalIncome: Int?,
        val totalExpense: Int?
    )

    data class WeekCategory(
        val income: List<WeekCategoryIncome>,
        val expense: List<WeekCategoryExpense>
    )

    data class WeekCategoryIncome(
        val category: String,
        val totalIncome: Int
    )

    data class WeekCategoryExpense(
        val category: String,
        val totalExpense: Int
    )
}


