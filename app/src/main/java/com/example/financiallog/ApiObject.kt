package com.example.financiallog

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiObject {
     var api : ApiInter
     val okHttpClient = OkHttpClient.Builder()
         .connectTimeout(110, TimeUnit.SECONDS)
         .readTimeout(100, TimeUnit.SECONDS)
         .writeTimeout(100, TimeUnit.SECONDS)
         .build()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ApiInter::class.java)
    }
}

/*interface ApiGetExpend {
    @GET("/calendar_app/getWallet?user=1&date=2024-04-09/")
    fun getExpendAll(
    ): Call<List<ExpendAdapter.Exlist>>
}

interface ApiGetIncome {
    @GET("/calendar_app/getWallet?user=1&date=2024-04-09/")
    fun getIncomeAll(
    ): Call<List<IncomeAdapter.IncomeList>>
}

interface ApiInsertExpend {
    @POST("wallet_app/saveExpense/")
    fun insertEx(@Body map: HashMap<String?, String?>?): Call<ExpendAdapter.Exlist?>?
}

interface ApiInsertIncome {
    @POST("wallet_app/saveIncome/")
    fun insertIn(@Body map: HashMap<String?, String?>?): Call<IncomeAdapter.IncomeList?>?
}*/









