package com.example.financiallog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.financiallog.ExpendAdapter.ExpendViewHolder as ExpendViewHolder1

class ExpendAdapter:RecyclerView.Adapter<ExpendViewHolder1>(){
    private var items = ArrayList<PostExpend>()
    class ExpendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catView1:TextView = itemView.findViewById<TextView>(R.id.category_tv)
        val shopView1:TextView = itemView.findViewById<TextView>(R.id.shop_tv)
        val payView1:TextView = itemView.findViewById<TextView>(R.id.expend_tv)

        fun setItem(item: PostExpend){
            catView1.text = item.category
            shopView1.text = item.banme
            payView1.text = item.price.toString()

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpendViewHolder1 {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.expend_list, parent, false)
        return ExpendViewHolder1(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun addItem(item: PostExpend) {
        items.add(item)
    }

    override fun onBindViewHolder(holder: ExpendViewHolder1, position: Int) {
        val exlist:PostExpend = items[position]
        holder.setItem(exlist)

    }

    data class Exlist(
        val tv_cateG_1: String,
        val tv_shopN_1: String,
        val tv_pay_1: Int,
        val tv_together_1: String,
        val tv_check_1: String,
        val tv_sat_1: String,
    )
}