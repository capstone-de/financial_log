package com.example.financiallog

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInter {
    @POST("wallet_app/saveExpense")
    fun insertEx(@Body map: HashMap<String, String>): Call<PostExpend>?
    @GET("/calendar_app/getWallet?user=1&date=2024-04-09")
    fun getExpendAll(
    ): Call<List<PostExpend>>

    @POST("wallet_app/saveIncome")
    fun insertIn(@Body map: HashMap<String, String>): Call<PostIncome>?
    @GET("/calendar_app/getWallet?user=1&date=2024-04-09")
    fun getIncomeAll(
    ): Call<List<PostIncome>>
}