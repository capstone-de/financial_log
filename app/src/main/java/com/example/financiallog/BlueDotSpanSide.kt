package com.example.financiallog

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import android.text.style.ReplacementSpan

class BlueDotSpanSide(private val color: Int, private val radius: Float, private val yOffset: Float): LineBackgroundSpan {
    override fun drawBackground(
        canvas: Canvas,
        paint: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        lineNumber: Int
    ) {
        val totalWidth = right - left
        val middle = left + totalWidth / 2

        // 파란색 점을 노란색 점의 오른쪽에 위치시키기 위해 middle 값에 반지름의 2배를 더합니다.
        val blueCircleX = middle + radius * 3

        val paint = Paint(paint)
        paint.color = color

        canvas.drawCircle(blueCircleX.toFloat(), bottom + radius + yOffset, radius, paint)
    }
}


