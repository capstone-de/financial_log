package com.example.financiallog

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInter {

    // 지출관련
    @POST("/wallet_app/saveExpense")
    fun insertEx(@Body map: HashMap<String, Any>): Call<PostExpend>?
    @GET("/calendar_app/getWalletExpense?")
    fun getExpendAll(
        @Query("user") userId: Int,
        @Query("date") date: String
    ): Call<ResponseExpend>
    //지출 함꼐한 사람
    @GET("wallet_app/saveExpense?user=6")
    fun getFollower(): Call<List<String>> // ResponseExFollower 대신 List<String>을 사용


    //수입관련
    @POST("/wallet_app/saveIncome")
    fun insertIn(@Body map: HashMap<String, String>): Call<PostIncome>?
    @GET("/calendar_app/getWalletIncome?user=6&date=2024-05-15")
    fun getIncomeAll(@Query("user") userId: Int,
                     @Query("date") date: String): Call<ResponseIncome>

    //달력
    @GET("/calendar_app/getCalendar?user=6&year=2024&month=5")
    fun getcalender():Call<ResponseCalendar>

    //일기관련
    @POST("/diary_app/saveDiary")
    fun insertDi(@Body map: HashMap<String, Any>): Call<PostDiary>?
    @GET("/diary_app/saveDiary?")
    fun diarywriteEx(@Query("user") userId: Int, @Query("date") date: String): Call<List<DataEx>>
    @GET("/diary_app/diaryList?user=6")
    fun getDiarylist(): Call<ArrayList<ResponseDiary>>
    @GET("/diary_app/myDiaryList?user=6")
    fun getDiaryMylist(): Call<ResponseMyDiary>

    //통계관련
    @GET("/statistics_app/daily")
    fun getStatisticsDaily(
        @Query("user") userId: Int,
        @Query("date") date: String
    ): Call<List<ResponseStatDay>> // 일별 (현재 작성 중인 act에서 이렇게 안하면 오류가 발생해 임시방편

    /*@GET("/statistics_app/daily?user=6&date=24-05-10}")
    fun getStatisticsDaily(): Call<List<ResponseStatDay>> //일별*/
    @GET("/statistics_app/weekly?user=6&date=24-05-10")
    fun getStatisticsWeekly(): Call<List<ResponseStatWeek>> //주별
    @GET("/statistics_app/monthly?user=6&year=2024&month=05")
    fun getStatisticsMonthly(): Call<List<ResponseStatMonth>> //월별
    @GET("/statistics_app/yearly?user=6&year=2024")
    fun getStatisticsYearly(): Call<List<ResponseStatYear>> //연별
    @GET("/wordcloud_app/myDiary?user=6")// 나의 현재 트렌드
    fun getStatisticsMyHashtag():Call<ResponseBody>
    @GET("/wordcloud_app/diary") //전체 트렌드
    fun getStatisticsHashtag():Call<ResponseBody>

    //로그인
    @GET("/login/signIn")
    fun signIn(): Call<ResponseExpend>

    //회원가입
    @POST("/login/signUp")
    fun signUp(@Body map: HashMap<String, Any>): Call<PostExpend>?
}