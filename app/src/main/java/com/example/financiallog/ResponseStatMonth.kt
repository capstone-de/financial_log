package com.example.financiallog

data class ResponseStatMonth (
    val jan: MonthStatistics,
    val feb: MonthStatistics,
    val mar: MonthStatistics,
    val apr: MonthStatistics,
    val may: MonthStatistics,
    val jun: MonthStatistics,
    /*val jul: MonthStatistics,
    val aug: MonthStatistics,
    val sep: MonthStatistics,
    val oct: MonthStatistics,
    val nov: MonthStatistics,
    val dec: MonthStatistics,*/

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

    data class Satisfaction(
        val satisfaction: SatisfactionItem?
    )

    data class SatisfactionItem(
        val category: String,
        val bname: String,
        val price: Int,
        val satisfaction: Int
    )

    data class MonthCategoryExpense(
        val category: String,
        val totalExpense: Int?
    )
<<<<<<< HEAD
}
=======
}
>>>>>>> f4e1deaa07d48dd614c2f63056791c8a9f026487
