package com.example.financiallog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiaryListAdapter : RecyclerView.Adapter<DiaryListAdapter.DiaryViewHolder>() {

    private val items = ArrayList<DiaryList>()

    class DiaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dwirte_view = itemView.findViewById<TextView>(R.id.feed_text)
        val tag_d = itemView.findViewById<TextView>(R.id.tag_dr)
        val tag_w = itemView.findViewById<TextView>(R.id.tag_1)

        fun setItem(item: DiaryList) {
            dwirte_view.text = item.content
            tag_d.text = item.tag
            tag_w.text = item.tag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.diary_list, parent, false)
        return DiaryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: DiaryList) {
        items.add(item)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    data class DiaryList(val content: String, val tag: String)
}