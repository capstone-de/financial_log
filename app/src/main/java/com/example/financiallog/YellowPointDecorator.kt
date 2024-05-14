package com.example.financiallog

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class YellowPointDecorator (private val date: String) : DayViewDecorator {
    override fun decorate(view: DayViewFacade?) {
        if (view!!.equals(date)) {
            view!!.addSpan(ForegroundColorSpan(Color.YELLOW))
        }
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return true
    }
}