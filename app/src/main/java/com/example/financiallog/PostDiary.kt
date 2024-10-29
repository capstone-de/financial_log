package com.example.financiallog

import android.media.Image
import java.nio.file.Files
import java.util.Date

data class PostDiary(
    val user_id : Int,
    val date : Date,
    val contents : String,
    val privacy : String,
    val hastag : String,
    val image : Image,
    val gu : String
    )
