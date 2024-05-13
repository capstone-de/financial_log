package com.example.financiallog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class DiaryMyListAdapter(private val data: ArrayList<ResponseMyDiary.DataMyDi>,
                         val data_1: ResponseMyDiary): RecyclerView.Adapter<DiaryMyListAdapter.DiaryViewHolder>() {
    class DiaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /*fun bind(item:ResponseMyDiary.DataNick){
            itemView.findViewById<TextView>(R.id.textView3).text = item.nickname
        }
        fun bind(item: ResponseMyDiary.DataFollwer){
            itemView.findViewById<TextView>(R.id.follow_num).text = item.follower.toString()

        }
        fun bind(item: ResponseMyDiary.DataFollowing){
            itemView.findViewById<TextView>(R.id.following_num).text = item.following.toString()
        }*/
        fun bind(item:ResponseMyDiary) {
            itemView.findViewById<TextView>(R.id.textView3).text = item.nickname
            itemView.findViewById<TextView>(R.id.follow_num).text = item.follower.toString()
            itemView.findViewById<TextView>(R.id.following_num).text = item.following.toString()

        }
        fun bind(item: ResponseMyDiary.DataMyDi){
            itemView.findViewById<TextView>(R.id.day_tv).text = item.date
            itemView.findViewById<TextView>(R.id.feed_text).text = item.contents
            itemView.findViewById<TextView>(R.id.tag_dr).text = item.hashtag.toString()
        }
        fun bind(item:ResponseMyDiary.Hashtag) {
            itemView.findViewById<TextView>(R.id.tag_dr).text = item.hashtag

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.diary_list, parent, false)
        return DiaryMyListAdapter.DiaryViewHolder(itemView)
        val itemView2 = LayoutInflater.from(parent.context).inflate(R.layout.mypage_home, parent, false)
        return DiaryViewHolder(itemView2)
    }

    override fun getItemCount(): Int {
        return data.size
        //return data_2.size
        //return data_3.size
    }

    override fun onBindViewHolder(holder: DiaryMyListAdapter.DiaryViewHolder, position: Int) {
        val item = data[position]
        val item_1 = data_1
        //val item_2 = data_2[position]
       // val item_3 = data_3[position]
        holder.bind(item)
        holder.bind(item_1)
        //holder.bind(item_2)
       // holder.bind(item_3)

    }
}