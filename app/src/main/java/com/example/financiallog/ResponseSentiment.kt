package com.example.financiallog

data class  ResponseSentiment (
    val count: Int,
    val coordinate: List<List<Double>>,
    val correlation: Double
)
