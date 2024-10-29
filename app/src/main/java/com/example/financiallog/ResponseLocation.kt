package com.example.financiallog

data class ResponseLocation (
    val coordinate: List<List<Double>>,
    val correlation: Double
)