package com.example.financiallog

data class ResponseStatYear(
    val Jan: YearStatistics,
    val Feb: YearStatistics,
    val Mar: YearStatistics,
    val Apr: YearStatistics,
    val May: YearStatistics,
    val Jun: YearStatistics,
    val Jul: YearStatistics,
    val Aug: YearStatistics,
    val Sep: YearStatistics,
    val Oct: YearStatistics,
    val Nov: YearStatistics,
    val Dec: YearStatistics,
    val category: YearlyCategory,
    val satisfaction: YearlySatisfaction
) {
    data class YearStatistics(
        val totalIncome: Int?,
        val totalExpense: Int?
    )

    data class YearlyCategory(
        val yearly: List<YearlyCategoryExpense>
    )

    data class YearlyCategoryExpense(
        val category: String,
        val totalExpense: Int
    )

    data class YearlySatisfaction(
        val yearly: List<YearlyCategorySatisfaction>
    )

    data class YearlyCategorySatisfaction(
        val category: String,
        val averageSatisfaction: Double
    )


}