package com.example.financiallog

data class ResponseStatHasgtag (
    val img :IMg
){
    data class IMg(
        val url: String,
        val description: String
    )
}