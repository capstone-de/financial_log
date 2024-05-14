package com.example.financiallog

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class RedPointDecorator(private val dateString: String) : DayViewDecorator {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val decoratorDate = LocalDate.parse(dateString, formatter)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        val dayDate = LocalDate.of(day.year, day.month, day.day) // CalendarDay는 0부터 시작하는 월을 사용합니다.
        return dayDate.isEqual(decoratorDate)
    }

    override fun decorate(view: DayViewFacade) {
        // DotSpan 생성자의 첫 번째 인자로 점의 색상을 지정합니다.
        // 여기서는 Color.YELLOW를 사용하였습니다.
        // 필요에 따라 색상을 변경하거나, 점의 크기를 지정하는 인자를 추가할 수 있습니다.
        view.addSpan(DotSpan(5f, Color.RED))  // 여기서 5f는 점의 반지름을 의미합니다. 필요에 따라 조절 가능합니다.
    }
}