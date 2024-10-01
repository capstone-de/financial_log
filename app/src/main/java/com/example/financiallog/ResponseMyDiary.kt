package com.example.financiallog

data class ResponseMyDiary(
    val nickname: String,
    val follower: Int,
    val following: Int,
    val myDiaryList:List<DataMyDi>
){
    data class DataMyDi(
        val date : String,
        val contents : String,
        val hashtag : List<String>,
        val image: List<String>
    )
}
