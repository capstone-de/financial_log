package com.example.financiallog

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import android.text.style.ReplacementSpan

class YellowDotSpanSide(private val color: Int, private val radius: Float, private val yOffset: Float):
    LineBackgroundSpan {
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

        val paint = Paint(paint)
        paint.color = color

        canvas.drawCircle(middle.toFloat(), bottom + radius + yOffset, radius, paint)
    }
}






