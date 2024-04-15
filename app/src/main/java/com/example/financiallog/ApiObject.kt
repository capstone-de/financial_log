package com.example.financiallog

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class ApiObject {

    var retrofitClient = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var getExpend: ApiGetExpend = retrofitClient.create(ApiGetExpend::class.java)
    var getIncome: ApiGetIncome = retrofitClient.create(ApiGetIncome::class.java)
    var insertExpend: ApiInsertExpend = retrofitClient.create(ApiInsertExpend::class.java)
    var insertIncome : ApiInsertIncome = retrofitClient.create(ApiInsertIncome::class.java)

    /*private val getRetrofit by lazy{
        Retrofit.Builder()
            .baseUrl(Companion.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getRetrofitService : ApiExpend by lazy { getRetrofit.create(ApiExpend::class.java) }*/

    companion object {
        private const val BASE_URL = "https://8000"
    }


}

interface ApiGetExpend {
    @GET("/calendar_app/getWallet?user=1&date=2024-04-09")
    fun getExpendAll(
    ): Call<List<ExpendAdapter.Exlist>>
}

interface ApiGetIncome {
    @GET("/calendar_app/getWallet?user=1&date=2024-04-09")
    fun getIncomeAll(
    ): Call<List<IncomeAdapter.IncomeList>>
}

interface ApiInsertExpend {
    @POST("wallet_app/saveExpense")
    fun insertEx(@Body map: HashMap<String?, String?>?): Call<ExpendAdapter.Exlist?>?
}

interface ApiInsertIncome {
    @POST("wallet_app/saveIncome")
    fun insertIn(@Body map: HashMap<String?, String?>?): Call<IncomeAdapter.IncomeList?>?
}






