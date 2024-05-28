package com.example.financiallog

data class ResponseStatYear(
    val jan: YearStatistics,
    val feb: YearStatistics,
    val mar: YearStatistics,
    val apr: YearStatistics,
    val may: YearStatistics,
    val jun: YearStatistics,
    val jul: YearStatistics,
    val aug: YearStatistics,
    val sep: YearStatistics,
    val oct: YearStatistics,
    val nov: YearStatistics,
    val dec: YearStatistics,
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