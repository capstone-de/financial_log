package com.example.financiallog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class DayExpenseAdapter(private var expenses: List<ResponseStatDay.DayExpense>) : RecyclerView.Adapter<DayExpenseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryTextView: TextView = view.findViewById(R.id.cate_name)
        val bnameTextView: TextView = view.findViewById(R.id.shop_name)
        val priceTextView: TextView = view.findViewById(R.id.text_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.analyzeday_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = expenses[position]
        //holder.categoryTextView.text = expense.category
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
        val category = expense.category
        holder.categoryTextView.text = categoryMap[category] ?: category
        holder.bnameTextView.text = expense.bname
        // 가격을 한국 통화 형식으로 변환
        val priceFormatted = NumberFormat.getNumberInstance(Locale.KOREA).format(expense.price)
        holder.priceTextView.text = priceFormatted
        //holder.priceTextView.text = expense.price.toString()
    }

    override fun getItemCount() = expenses.size

    fun updateData(newExpenses: List<ResponseStatDay.DayExpense>) {
        expenses = newExpenses
        notifyDataSetChanged()
    }
}


