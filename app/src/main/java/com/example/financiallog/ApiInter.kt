package com.example.financiallog

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInter {

    // 지출관련
    @POST("/wallet_app/saveExpense")
    fun insertEx(@Body map: HashMap<String, Any>): Call<PostExpend>?
    @GET("/calendar_app/getWallet?user=1&date=2024-04-09")
    fun getExpendAll(): Call<List<PostExpend>>

    //수입관련
    @POST("/wallet_app/saveIncome")
    fun insertIn(@Body map: HashMap<String, String>): Call<PostIncome>?
    @GET("/calendar_app/getWallet?user=1&date=2024-04-09")
    fun getIncomeAll(): Call<ResponseIncome>

    //일기관련
    @POST("/diary/saveDiary")
    fun insertDi(@Body map: HashMap<String, Any>): Call<PostDiary>?
    @GET("/diary_app/saveDiary?user=1&date=2024-04-09")
    fun diarywriteEx(): Call<List<PostDiary>>
    @GET("/diary_app/diaryList?user=user_id")
    fun getDiarylist(): Call<List<PostDiary>>
    @GET("/diary_app/myDiaryList?user=1")
    fun getDiaryMylist(): Call<List<PostDiary>>
}