package com.uphar.bussinesss.domain.Utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DateTimeUtils {
    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS
    const val SERVER_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val MMM_DD_YYYY_HH_MM_A_TIME_FORMAT = "MMM dd, yyyy hh:mm a"
    const val DD_MMM_YYYY_TIME_FORMAT = "dd MMM yyyy"

    @JvmStatic
    fun getFormattedDate(
        timestamp: String,
        inputFormat: String = SERVER_TIME_FORMAT,
        outPutFormat: String = MMM_DD_YYYY_HH_MM_A_TIME_FORMAT,
        isTimeAgo: Boolean = false
    ): String? {
//        val timestampFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
//        val outputFormat = "MMM dd, yyyy hh:mm a"

        val dateFormatter = SimpleDateFormat(outPutFormat, Locale.getDefault())
        dateFormatter.timeZone = TimeZone.getTimeZone("GMT")

        val parser = SimpleDateFormat(inputFormat, Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("GMT")

        try {
            val date = parser.parse(timestamp)
            if (date != null) {
                if (isTimeAgo) {
                    val timeAgo = getTimeAgo(date.time)
                    if (timeAgo != null) {
                        return timeAgo
                    }
                }
                dateFormatter.timeZone = TimeZone.getDefault()
                return dateFormatter.format(date)
            }
        } catch (e: Exception) {
            // Handle parsing error
            e.printStackTrace()
        }
        // If parsing fails, return the original timestamp
        return timestamp
    }

    private fun getTimeAgo(time: Long): String? {
        var time = time
        if (time < 1000000000000L) {
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }
        val diff = now - time
        return if (diff < MINUTE_MILLIS) {
            "just now"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "a minute ago"
        } else if (diff < 50 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " minutes ago"
        } else if (diff < 90 * MINUTE_MILLIS) {
            "an hour ago"
        } else if (diff < 24 * HOUR_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " hours ago"
        } else if (diff < 48 * HOUR_MILLIS) {
            "yesterday"
        } else {
            null
//            (diff / DAY_MILLIS).toString() + " days ago"
        }
    }
}