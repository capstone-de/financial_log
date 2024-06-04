package com.example.financiallog

data class ResponseStatWeek (
    val mon: DayStatistics,
    val tue: DayStatistics,
    val wed: DayStatistics,
    val thu: DayStatistics,
    val fri: DayStatistics,
    val sat: DayStatistics,
    val sun: DayStatistics,

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


