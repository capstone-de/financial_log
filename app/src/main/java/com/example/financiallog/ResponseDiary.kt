package com.example.financiallog

import java.util.ArrayList
import java.util.Date

data class ResponseDiary(
    val nickname : String,
    val date : String,
    val contents : String,
    val hashtag : List<String>,
    val image: List<String>,
    val gu :String
)

