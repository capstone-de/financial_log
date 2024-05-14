package com.example.financiallog

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class DiaryListAdapter(private val data: ResponseDiary,
    private val data_2:ArrayList<ResponseDiary.DataDiary>) : RecyclerView.Adapter<DiaryListAdapter.DiaryViewHolder>() {

    //private val items = ArrayList<DiaryList>()

    class DiaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ResponseDiary){
            itemView.findViewById<TextView>(R.id.day_tv).text = item.date.toString()
            itemView.findViewById<TextView>(R.id.feed_text).text = item.contents
            itemView.findViewById<TextView>(R.id.nickname_tv).text = item.nickname
            itemView.findViewById<TextView>(R.id.tag_dr).text = item.hashtag.toString()


        }
        fun bind(item: ResponseDiary.DataDiary){
            itemView.findViewById<TextView>(R.id.tag_dr).text = item.hashtag


        }

       /* fun setItem(item: ResponseDiary) {
            dwirte_view.text = item.content
            tag_d.text = item.tag
            tag_w.text = item.tag
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.diary_list, parent, false)
        return DiaryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data_2.size
    }

    //fun addItem(item: DiaryList) {
    //    data.add(item)
   // }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val item = data
        val item_1 = data_2[position]
        holder.bind(item)
        holder.bind(item_1)
    }

    data class DiaryList(val content: String, val tag: String)
}