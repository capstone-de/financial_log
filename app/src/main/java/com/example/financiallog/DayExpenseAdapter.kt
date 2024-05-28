package com.example.financiallog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
        holder.categoryTextView.text = expense.category
        holder.bnameTextView.text = expense.bname
        holder.priceTextView.text = expense.price.toString()
    }

    override fun getItemCount() = expenses.size

    fun updateData(newExpenses: List<ResponseStatDay.DayExpense>) {
        expenses = newExpenses
        notifyDataSetChanged()
    }
}


