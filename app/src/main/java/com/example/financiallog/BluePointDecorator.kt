package com.example.financiallog

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class BluePointDecorator(private val dateString: String) : DayViewDecorator {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val decoratorDate = LocalDate.parse(dateString, formatter)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        val dayDate = LocalDate.of(day.year, day.month , day.day) // CalendarDay는 0부터 시작하는 월을 사용합니다. +1을 해줍니다.
        return dayDate.isEqual(decoratorDate)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(BlueDotSpanSide(Color.parseColor("#4c8df7"), 7f, 10f)) // 파란색 점
    }
}


