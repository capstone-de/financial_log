package com.example.financiallog

import java.nio.file.Files
import java.util.Date

data class PostDiary(
    val user_id : Int,
    val date : Date,
    val contents : String,
    val privacy : String,
    val hastag : String,
    val file : Files

    )
