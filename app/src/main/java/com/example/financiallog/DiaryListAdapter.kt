package com.example.financiallog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.BitmapFactory
import android.util.Base64

class DiaryListAdapter(private val dataList: ArrayList<ResponseDiary>) : RecyclerView.Adapter<DiaryListAdapter.DiaryViewHolder>() {

    class DiaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ResponseDiary) {
            itemView.findViewById<TextView>(R.id.day_tv).text = item.date
            itemView.findViewById<TextView>(R.id.feed_text).text = item.contents
            itemView.findViewById<TextView>(R.id.nickname_view).text = item.nickname
            itemView.findViewById<TextView>(R.id.tag_dr).text = item.hashtag.joinToString(", ") // 해시태그 리스트를 문자열로 변환
            val imageView = itemView.findViewById<ImageView>(R.id.feed_image)

            // image가 리스트인 경우 첫 번째 이미지를 사용
            if (item.image.isNotEmpty()) {
                val firstImageBase64 = item.image[0]

                // Base64 문자열을 디코딩하여 비트맵으로 변환
                val decodedBytes = Base64.decode(firstImageBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                // 비트맵을 ImageView에 설정
                imageView.setImageBitmap(bitmap)
                imageView.visibility = View.VISIBLE
            } else {
                // 이미지 리스트가 비어있으면 ImageView를 숨김
                imageView.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feed_list, parent, false)
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
