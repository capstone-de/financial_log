package com.example.financiallog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class DiaryMyListAdapter(private val data: ArrayList<ResponseMyDiary.DataMyDi>,
                         private val profileData: ResponseMyDiary): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // 뷰 타입을 위한 상수 정의
    companion object {
        const val TYPE_PROFILE = 0
        const val TYPE_DIARY = 1
    }

    // 각 아이템의 뷰 타입 결정
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_PROFILE else TYPE_DIARY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PROFILE -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mypage_home, parent, false)
                ProfileViewHolder(itemView)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.diary_list, parent, false)
                DiaryViewHolder(itemView)
            }
        }
    }

    override fun getItemCount(): Int {
        // 프로필 정보도 하나의 아이템으로 취급
        return data.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_PROFILE -> (holder as ProfileViewHolder).bind(profileData)
            TYPE_DIARY -> (holder as DiaryViewHolder).bind(data[position - 1]) // 첫 번째 아이템은 프로필 정보이므로 position에서 1을 빼줍니다.
        }
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(profileData: ResponseMyDiary) {
            itemView.findViewById<TextView>(R.id.textView3).text = profileData.nickname
            itemView.findViewById<TextView>(R.id.follow_num).text = profileData.follower.toString()
            itemView.findViewById<TextView>(R.id.following_num).text = profileData.following.toString()
        }
    }

    class DiaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(diaryData: ResponseMyDiary.DataMyDi) {
            itemView.findViewById<TextView>(R.id.day_tv).text = diaryData.date
            itemView.findViewById<TextView>(R.id.feed_text).text = diaryData.contents
            itemView.findViewById<TextView>(R.id.tag_dr).text = diaryData.hashtag.toString()
        }
    }
}
