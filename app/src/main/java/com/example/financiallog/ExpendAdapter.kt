package com.example.financiallog

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import com.example.financiallog.ExpendAdapter.ExpendViewHolder as ExpendViewHolder1

class ExpendAdapter(private val data: ArrayList<ResponseExpend.DataEx>):RecyclerView.Adapter<ExpendViewHolder1>(){
   // private var items = ArrayList<PostExpend>()



    class ExpendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ResponseExpend.DataEx){
            // 카테고리 매핑 설정
            val categoryMap = mapOf(
                "tax" to "세금",
                "food" to "음식",
                "housing/communication" to "주거/통신",
                "tranportation/vehicle" to "교통/차량",
                "education" to "교육",
                "personal event" to "경조사/회비",
                "medical" to "병원/약국",
                "cultural/living" to "문화생활",
                "shopping" to "쇼핑",
                "etc" to "기타"
            )

            // 카테고리를 변환하여 TextView에 설정
            val categoryTextView = itemView.findViewById<TextView>(R.id.category_tv)
            val category = item.category
            val koreanCategory = categoryMap[category] ?: category // 매핑이 없으면 원래 카테고리를 사용
            categoryTextView.text = koreanCategory

            itemView.findViewById<TextView>(R.id.shop_tv).text = item.bname
            itemView.findViewById<TextView>(R.id.expend_tv).text = item.price.toString()
            // 위치 정보를 저장할 배열을 준비합니다.
            val location = IntArray(2)
            // itemView의 화면 상 위치를 가져옵니다.
            itemView.getLocationOnScreen(location)
            // 위치 정보를 사용하여 로그를 출력합니다.
            Log.d("Expend ItemPosition", "category: ${koreanCategory}, bname: ${item.bname}, price: ${item.price}, x: ${location[0]}, y: ${location[1]}")
        }


        //val catView1:TextView = itemView.findViewById<TextView>(R.id.category_tv)
        //val shopView1:TextView = itemView.findViewById<TextView>(R.id.shop_tv)
        //val payView1:TextView = itemView.findViewById<TextView>(R.id.expend_tv)

       /* fun setItem(item: PostExpend){
            catView1.text = item.category
            shopView1.text = item.bname
            payView1.text = item.price.toString()

        }*/

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpendViewHolder1 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expend_list, parent, false)
        return ExpendViewHolder1(view)
    }

    override fun getItemCount() = data.size

    /*fun addItem(item: PostExpend) {
        items.add(item)
    }*/

    override fun onBindViewHolder(holder: ExpendViewHolder1, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

   /* data class Exlist(
        val tv_cateG_1: String,
        val tv_shopN_1: String,
        val tv_pay_1: Int,
        val tv_together_1: String,
        val tv_check_1: String,
        val tv_sat_1: String,
    )*/
}