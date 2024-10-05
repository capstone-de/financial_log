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

            itemView.findViewById<TextView>(R.id.tag_dr).text = if (item.hashtag.isEmpty()) {
                ", "
            } else {
                "#" + item.hashtag.joinToString(" #")
            }

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

            itemView.findViewById<TextView>(R.id.tag_dr).text = if (item.hashtag.isEmpty()) {
                " "
            } else {
                "#" + item.hashtag.joinToString(" #")
            }

            // 이미지 뷰는 필요 없으므로 숨김 처리
           // itemView.findViewById<ImageView>(R.id.feed_image).visibility = View.GONE
        }
    }

    /*class DiaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ResponseDiary) {
            // 날짜 형식을 변환하여 TextView에 설정
            val dateFormatInput = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val dateFormatOutput = SimpleDateFormat("yyyy.MM.dd E요일", Locale.KOREA)
            val date = dateFormatInput.parse(item.date)
            val formattedDate = dateFormatOutput.format(date)

            itemView.findViewById<TextView>(R.id.day_tv).text = formattedDate
            //itemView.findViewById<TextView>(R.id.day_tv).text = item.date
            itemView.findViewById<TextView>(R.id.feed_text).text = item.contents
            itemView.findViewById<TextView>(R.id.nickname_view).text = item.nickname
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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feed_list, parent, false)
        return DiaryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size // 데이터 리스트의 크기 반환
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val item = dataList[position] // 현재 위치의 데이터 아이템 가져오기
        holder.bind(item)
    }*/
}
