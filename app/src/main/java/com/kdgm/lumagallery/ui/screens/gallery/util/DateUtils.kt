package com.kdgm.lumagallery.ui.screens.gallery.util

import java.text.SimpleDateFormat
import java.util.*

fun getDateLabel(timeMillis: Long): String {
    val today = Calendar.getInstance()
    val target = Calendar.getInstance().apply { timeInMillis = timeMillis }

    return when {
        isSameDay(today, target) -> "Today"
        isYesterday(today, target) -> "Yesterday"
        else -> {
            val formatter = SimpleDateFormat("MMM dd", Locale.getDefault())
            formatter.format(Date(timeMillis))
        }
    }
}

private fun isSameDay(c1: Calendar, c2: Calendar): Boolean =
    c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
            c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)

private fun isYesterday(today: Calendar, target: Calendar): Boolean {
    today.add(Calendar.DAY_OF_YEAR, -1)
    val result = isSameDay(today, target)
    today.add(Calendar.DAY_OF_YEAR, +1)
    return result
}
