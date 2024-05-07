package com.example.financiallog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IncomeAdapter(private val data: List<ResponseIncome.DataIn>): RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder>() {

    class IncomeViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ResponseIncome.DataIn){
            itemView.findViewById<TextView>(R.id.category_tv).text = item.category
            itemView.findViewById<TextView>(R.id.expend_tv).text = item.price.toString()
        }
        //val catView2 = itemView.findViewById<TextView>(R.id.category_tv)
        //val payView2 = itemView.findViewById<TextView>(R.id.expend_tv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.income_list, parent, false)
        return IncomeViewHolder(view)
    }

    override fun getItemCount() = data.size


    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

}


