package com.example.financiallog

data class ResponseStatMonth (
    val jan: MonthStatistics,
    val feb: MonthStatistics,
    val mar: MonthStatistics,
    val apr: MonthStatistics,


    val category: MonthlyCategory,
    val satisfaction: Satisfaction

) {
    data class MonthStatistics(
        val totalIncome: Int?,
        val totalExpense: Int?
    )

    data class MonthlyCategory(
        val lastMonth: List<MonthCategoryExpense>,
        val thisMonth: List<MonthCategoryExpense>
    )

    data class MonthCategoryExpense(
        val category: String,
        val totalExpense: Int?
    )

    data class Satisfaction(
        val satisfaction: SatisfactionItem?
    )

    data class SatisfactionItem(
        val category: String,
        val bname: String,
        val price: Int,
        val satisfaction: Int
    )


}