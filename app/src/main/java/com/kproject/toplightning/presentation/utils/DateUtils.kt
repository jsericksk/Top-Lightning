package com.kproject.toplightning.presentation.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private const val DEFAULT_DATE_TIME_PATTERN = "HH:mm - dd MMM yyyy"

    fun getRelativeTimeSpan(
        unixTime: Long,
        dateTimePattern: String = DEFAULT_DATE_TIME_PATTERN
    ): String {
        val date = unixTime.unixTimeToDate()
        val relativeTime = DateUtils.getRelativeTimeSpanString(
            date.time,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
        val dateAndTime = getFormattedDateTime(unixTime, dateTimePattern)
        return "$relativeTime ($dateAndTime)"
    }

    fun getFormattedDateTime(
        unixTime: Long,
        dateTimePattern: String = DEFAULT_DATE_TIME_PATTERN
    ): String {
        val date = unixTime.unixTimeToDate()
        val simpleDateFormat = SimpleDateFormat(dateTimePattern, Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    private fun Long.unixTimeToDate() = Date(this * 1000L)
}