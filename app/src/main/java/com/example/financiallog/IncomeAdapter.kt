package com.example.financiallog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.financiallog.IncomeAdapter.IncomeViewHolder as IncomeViewHolder1

class IncomeAdapter(private val data: List<ResponseIncome.DataIn>): RecyclerView.Adapter<IncomeViewHolder1>() {

    class IncomeViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ResponseIncome.DataIn){

            // 카테고리 매핑 설정
            val categoryMap = mapOf(
                "salary" to "월급",
                "side income" to "부수입",
                "financial income" to "금융수익",
                "etc" to "기타",
            )

            /*val catView2 = itemView.findViewById<TextView>(R.id.category_tv)
            val payView2 = itemView.findViewById<TextView>(R.id.expend_tv)
            catView2.setText(item.category)
            payView2.setText(item.price)*/

            // 카테고리를 변환하여 TextView에 설정
            val categoryTextView = itemView.findViewById<TextView>(R.id.category_tv)
            val category = item.category
            val koreanCategory = categoryMap[category] ?: category // 매핑이 없으면 원래 카테고리를 사용
            categoryTextView.text = koreanCategory
            itemView.findViewById<TextView>(R.id.expend_tv).text = item.price.toString()
        }
        //val catView2 = itemView.findViewById<TextView>(R.id.category_tv)
        //val payView2 = itemView.findViewById<TextView>(R.id.expend_tv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder1 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.income_list, parent, false)
        return IncomeViewHolder1(view)
    }

    override fun getItemCount() = data.size


    override fun onBindViewHolder(holder: IncomeViewHolder1, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

}

