package com.example.financiallog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import java.text.SimpleDateFormat
import java.util.Locale

class DiaryMyListAdapter(private val data: List<ResponseMyDiary.DataMyDi>): RecyclerView.Adapter<DiaryMyListAdapter.DiaryViewHolder>() {

    class DiaryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item:ResponseMyDiary.DataMyDi){
            // 날짜 형식을 변환하여 TextView에 설정
            val dateFormatInput = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val dateFormatOutput = SimpleDateFormat("yyyy.MM.dd E요일", Locale.KOREA)
            val date = dateFormatInput.parse(item.date)
            val formattedDate = dateFormatOutput.format(date)

            itemView.findViewById<TextView>(R.id.day_tv).text = formattedDate
            //itemView.findViewById<TextView>(R.id.day_tv).text = item.date
            itemView.findViewById<TextView>(R.id.feed_text).text = item.contents
            if (item.hashtag.isEmpty())
                itemView.findViewById<TextView>(R.id.tag_dr).text = item.hashtag.joinToString(", ") // 해시태그 리스트를 문자열로 변환
            else
                itemView.findViewById<TextView>(R.id.tag_dr).text = "#" + item.hashtag.joinToString(" #") // 해시태그 리스트를 문자열로 변환
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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.diary_list, parent, false)
        return DiaryViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder:DiaryViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }


}
