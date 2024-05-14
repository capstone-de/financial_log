package com.example.financiallog

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInter {

    // 지출관련
    @POST("/wallet_app/saveExpense")
    fun insertEx(@Body map: HashMap<String, Any>): Call<PostExpend>?
    @GET("/calendar_app/getWalletExpense?user=4&date=2024-05-07")
    fun getExpendAll(): Call<ResponseExpend>
    //지출 함꼐한 사람
    @GET("wallet_app/saveExpense?user=6")
    fun getFollower(): Call<List<String>> // ResponseExFollower 대신 List<String>을 사용


    //수입관련
    @POST("/wallet_app/saveIncome")
    fun insertIn(@Body map: HashMap<String, String>): Call<PostIncome>?
    @GET("/calendar_app/getWalletIncome?user=4&date=2024-05-07")
    fun getIncomeAll(): Call<ResponseIncome>

    //달력
    @GET("/calendar_app/getCalendar?user=1&year=2024&month=4")
    fun getcalender():Call<ResponseCalender>

    //일기관련
    @POST("/diary/saveDiary")
    fun insertDi(@Body map: HashMap<String, Any>): Call<PostDiary>?
    @GET("/diary_app/saveDiary?user=1&date=2024-04-09")
    fun diarywriteEx(): Call<ResponseExpend>
    @GET("/diary_app/diaryList?user=user_id")
    fun getDiarylist(): Call<ResponseDiary>
    @GET("/diary_app/myDiaryList?user=4")
    fun getDiaryMylist(): Call<ResponseMyDiary>

    //통계관련
    @GET("/statistics/daily/{user?date}")
    fun getStatisticsDaily(): Call<List<ResponseStatDay>> //일별
    @GET("/statistics/weekly/{user?date}")
    fun getStatisticsWeekly(): Call<List<ResponseStatWeek>> //주별
    @GET("/statistics/monthly/{user?year?month}")
    fun getStatisticsMonthly(): Call<List<ResponseStatMonth>> //월별
    @GET("/statistics/yearly/{user?year}")
    fun getStatisticsYearly(): Call<List<ResponseStatYear>> //연별

    //로그인
    @GET("/login/signIn")
    fun signIn(): Call<ResponseExpend>

    //회원가입
    @POST("/login/signUp")
    fun signUp(@Body map: HashMap<String, Any>): Call<PostExpend>?
}