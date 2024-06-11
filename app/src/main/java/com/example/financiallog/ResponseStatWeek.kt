package com.example.financiallog

data class ResponseStatWeek (
    val Mon: DayStatistics,
    val Tue: DayStatistics,
    val Wed: DayStatistics,
    val Thu: DayStatistics,
    val Fri: DayStatistics,
    val Sat: DayStatistics,
    val Sun: DayStatistics,
    val category: WeekCategory
) {
    data class DayStatistics(
        val total_income: Int?,
        val total_expense: Int?
    )

    data class WeekCategory(
        val income: List<WeekCategoryIncome>,
        val expense: List<WeekCategoryExpense>
    )

    data class WeekCategoryIncome(
        val category: String,
        val total_income: Int
    )

    data class WeekCategoryExpense(
        val category: String,
        val total_expense: Int
    )
}