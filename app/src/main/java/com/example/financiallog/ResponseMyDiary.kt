package com.example.financiallog

data class ResponseMyDiary(
    val nickname: String,
    val follower: Int,
    val following: Int,
    val myDiaryList:ArrayList<DataMyDi>


){
    /*data class DataNick(
        val nickname :String
    )
    data class DataFollwer(
        val follower :Int
    )
    data class DataFollowing(
        val following: Int
    )*/
    data class DataMyDi(
        val date : String,
        val contents : String,
        val hashtag : ArrayList<Hashtag>
    )
    data class Hashtag(
        val hashtag :String
    )
}
