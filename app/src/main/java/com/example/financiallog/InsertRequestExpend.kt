package com.example.financiallog

import android.content.Context
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import java.util.Date

class InsertRequestExpend(
    user:Int, price:String, category : String, bname:String, with_whom:String, satisfaction:String,
    listener:Response.Listener<String?>)
    : StringRequest(Method.POST, URL, listener, null){
    private val applicationContext: Context? = null
    private val map: MutableMap<String, String>

    init{
        map = HashMap()
        map["user"] = user.toString()
        //map["date"] = date.toString()
        map["price"] = price
        map["category"] = category
        map["bname"] = bname
        map["with_whom"] = with_whom
        map["satisfaction"] = satisfaction
    }
    val listener = Response.Listener<String?> { response ->
        Toast.makeText(applicationContext, "데이터 전송이 완료되었습니다.", Toast.LENGTH_SHORT).show()
    }

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String?>{
        return map
    }
    companion object{
        private const val URL ="http://8000/wallet_app/saveExpense"
    }


}