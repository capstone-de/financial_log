package com.example.financiallog

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class CustomDayViewDecorator(private val color: Int, private val dates: Collection<CalendarDay>) :
    DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        // 여기에 지출, 수입, 일기 내역에 따라 해당 날짜에 점을 표시할지 여부를 결정하는 로직을 작성합니다.
        // 예를 들어, 해당 날짜에 지출 내역이 있다면 true를 반환하도록 작성할 수 있습니다.
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(5F, color)) // 점의 크기와 색상을 설정합니다.
    }
}