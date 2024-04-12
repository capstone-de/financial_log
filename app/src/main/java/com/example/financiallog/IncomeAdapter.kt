package com.example.financiallog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IncomeAdapter: RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder>() {
    private var items = ArrayList<IncomeList>()

    class IncomeViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catView2 = itemView.findViewById<TextView>(R.id.category_tv)
        val payView2 = itemView.findViewById<TextView>(R.id.expend_tv)

        fun setItem(item: IncomeList){
            catView2.text = item.tv_cateG_2
            payView2.text = item.tv_pay_2.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.income_list, parent, false)
        return IncomeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun addItem(item: IncomeList) {
        items.add(item)
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        val inlist: IncomeList = items[position]
        holder.setItem(inlist)
    }
    data class IncomeList(
        val tv_cateG_2: String,
        val tv_pay_2: Int,
    )
}


