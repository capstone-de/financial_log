package com.example.financiallog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.BitmapFactory
import android.util.Base64
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class DiaryListAdapter(private val dataList: ArrayList<ResponseDiary>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_WITH_IMAGE = 1
        private const val TYPE_WITHOUT_IMAGE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataList[position].image.isNotEmpty()) {
            TYPE_WITH_IMAGE
        } else {
            TYPE_WITHOUT_IMAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_WITH_IMAGE) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feed_list, parent, false)
            DiaryViewHolderWithImage(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feed_list_without_image, parent, false)
            DiaryViewHolderWithoutImage(itemView)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        if (holder is DiaryViewHolderWithImage) {
            holder.bind(item)
        } else if (holder is DiaryViewHolderWithoutImage) {
            holder.bind(item)
        }
    }

    // 이미지가 있는 경우의 ViewHolder
    class DiaryViewHolderWithImage(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ResponseDiary) {
            val dateFormatInput = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val dateFormatOutput = SimpleDateFormat("yyyy.MM.dd E요일", Locale.KOREA)
            val date = dateFormatInput.parse(item.date)
            val formattedDate = dateFormatOutput.format(date)

            itemView.findViewById<TextView>(R.id.day_tv).text = formattedDate
            itemView.findViewById<TextView>(R.id.feed_text).text = item.contents
            itemView.findViewById<TextView>(R.id.nickname_view).text = item.nickname
            itemView.findViewById<TextView>(R.id.location_tv).text = item.gu
            itemView.findViewById<ImageView>(R.id.location_im)

            // 해시태그 생성
            val hashtags = if (item.hashtag.isNotEmpty()) {
                item.hashtag.flatMap { it.split(",").map { hashtag -> "#${hashtag.trim()}" } } // 각 해시태그를 개별적으로 처리
                    .joinToString(", ") // 쉼표로 구분
            } else {
                "" // 해시태그가 없을 경우 빈 문자열 추가
            }
            itemView.findViewById<TextView>(R.id.tag_dr).text = hashtags

            val imageView = itemView.findViewById<ImageView>(R.id.feed_image)

            // 이미지 URL을 사용하여 Glide로 이미지 로드
            if (item.image.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(item.image[0]) // URL 로드
                    .error(R.drawable.btn_x) // 오류 시 대체 이미지 설정
                    .into(imageView) // ImageView에 설정
                imageView.visibility = View.VISIBLE
            }
        }
    }

    // 이미지가 없는 경우의 ViewHolder
    class DiaryViewHolderWithoutImage(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ResponseDiary) {
            val dateFormatInput = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val dateFormatOutput = SimpleDateFormat("yyyy.MM.dd E요일", Locale.KOREA)
            val date = dateFormatInput.parse(item.date)
            val formattedDate = dateFormatOutput.format(date)

            itemView.findViewById<TextView>(R.id.day_tv).text = formattedDate
            itemView.findViewById<TextView>(R.id.feed_text).text = item.contents
            itemView.findViewById<TextView>(R.id.nickname_view).text = item.nickname
            itemView.findViewById<TextView>(R.id.location_tv).text = item.gu
            itemView.findViewById<ImageView>(R.id.location_im)

            // 해시태그 생성
            val hashtags = if (item.hashtag.isNotEmpty()) {
                item.hashtag.flatMap { it.split(",").map { hashtag -> "#${hashtag.trim()}" } } // 각 해시태그를 개별적으로 처리
                    .joinToString(", ") // 쉼표로 구분
            } else {
                "" // 해시태그가 없을 경우 빈 문자열 추가
            }
            itemView.findViewById<TextView>(R.id.tag_dr).text = hashtags

        }
    }
}
