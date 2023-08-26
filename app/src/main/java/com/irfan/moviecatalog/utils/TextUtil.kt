package com.irfan.moviecatalog.utils

import com.irfan.moviecatalog.utils.Constant.YT_THUMBNAIL
import java.text.SimpleDateFormat
import java.util.Locale

object TextUtil {
    fun generateYoutubeThumbnail(videoId: String): String {
        return "${YT_THUMBNAIL}${videoId}/0.jpg"
    }

    fun dateConverter(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }
}