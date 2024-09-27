package com.example.imageapp.util

import java.text.SimpleDateFormat
import java.util.*

object DateAndTimeUtils {
    fun getDateFromTimestamp(timestamp: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC") // To handle UTC timestamps

        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date: Date = inputFormat.parse(timestamp) ?: return ""
        return outputFormat.format(date)
    }
}
