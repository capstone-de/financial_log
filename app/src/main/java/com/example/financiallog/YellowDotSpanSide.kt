package com.example.financiallog

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.ReplacementSpan

class YellowDotSpanSide(private val radius: Float) : ReplacementSpan() {
    private val color = Color.BLACK // Use a predefined color for the dot.

    override fun getSize(
        paint: Paint, text: CharSequence?, start: Int, end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        // The width is just the width of the text itself, as the dot will be drawn outside of this area.
        return paint.measureText(text, start, end).toInt()
    }

    override fun draw(
        canvas: Canvas, text: CharSequence?, start: Int, end: Int,
        x: Float, top: Int, y: Int, bottom: Int, paint: Paint
    ) {

        // Calculate the position of the dot.
        val lineMiddle = (top + bottom) / 2 // Find the vertical middle of the line.
        val circleX = x - radius * 3 // Move the dot to the left of the text. Adjust as necessary.
        val circleY = top + (bottom - top) / 3f // Place the dot at 1/3rd the height of the text.

        // Set up paint for the dot.
        val dotPaint = Paint(paint)
        dotPaint.color = color

        // Draw the dot.
        canvas.drawCircle(circleX, circleY, radius, dotPaint)

        // Draw the text as usual.
        canvas.drawText(text!!, start, end, x, y.toFloat(), paint)
    }
}
