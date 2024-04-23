package com.example.financiallog

import java.util.Date

data class PostIncome(
    val user_id : Int,
    val date : Date,
    val pay_tv_2 : Int,
    val tv_cateG_2: String,
)