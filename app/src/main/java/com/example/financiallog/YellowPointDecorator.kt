package com.example.financiallog

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class YellowPointDecorator(private val dateString: String) : DayViewDecorator {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val decoratorDate = LocalDate.parse(dateString, formatter)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        val dayDate = LocalDate.of(day.year, day.month, day.day) // CalendarDay는 0부터 시작하는 월을 사용합니다.
        return dayDate.isEqual(decoratorDate)
    }

    override fun decorate(view: DayViewFacade) {
        // Specify the radius of the dot and add the custom span to the DayViewFacade.
        view.addSpan(YellowDotSpanSide(Color.parseColor("#e56717"), 8f, 10f)) // Adjust the radius as needed.
    }

}