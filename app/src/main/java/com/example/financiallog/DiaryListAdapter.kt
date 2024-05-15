package com.example.financiallog

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class DiaryListAdapter(private val dataList: ArrayList<ResponseDiary>) : RecyclerView.Adapter<DiaryListAdapter.DiaryViewHolder>() {

    class DiaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ResponseDiary) {
            itemView.findViewById<TextView>(R.id.day_tv).text = item.date
            itemView.findViewById<TextView>(R.id.feed_text).text = item.contents
            itemView.findViewById<TextView>(R.id.nickname_tv).text = item.nickname
            itemView.findViewById<TextView>(R.id.tag_dr).text = item.hashtag.joinToString(", ") // 해시태그 리스트를 문자열로 변환
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.diary_list, parent, false)
        return DiaryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size // 데이터 리스트의 크기 반환
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val item = dataList[position] // 현재 위치의 데이터 아이템 가져오기
        holder.bind(item)
    }
}
