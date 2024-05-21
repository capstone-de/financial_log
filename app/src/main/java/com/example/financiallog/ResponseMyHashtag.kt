package com.example.financiallog

data class ResponseMyHashtag (
    val img :IMG
){
    data class IMG(
        val url: String,
        val description: String
    )
}