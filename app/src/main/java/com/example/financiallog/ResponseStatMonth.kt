package com.example.financiallog

data class ResponseStatMonth (
    val Jan: MonthStatistics? = null,
    val Feb: MonthStatistics? = null,
    val Mar: MonthStatistics? = null,
    val Apr: MonthStatistics? = null,
    val May: MonthStatistics? = null,
    val Jun: MonthStatistics? = null,
    val Jul: MonthStatistics? = null,
    val Aug: MonthStatistics? = null,
    val Sep: MonthStatistics? = null,
    val Oct: MonthStatistics? = null,
    val Nov: MonthStatistics? = null,
    val Dec: MonthStatistics? = null,

    val category: MonthlyCategory = MonthlyCategory(emptyList(), emptyList()), // 기본값 설정
    val satisfaction: Satisfaction? = null

) {
    data class MonthStatistics(
        val total_income: Int?,
        val total_expense: Int?
    )

    data class MonthlyCategory(
        val lastMonth: List<MonthCategoryExpense>,
        val thisMonth: List<MonthCategoryExpense>
    )

    data class MonthCategoryExpense(
        val category: String,
        val total_expense: Int?
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