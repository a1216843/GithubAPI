package com.example.githubapi.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    val dateFormatToShow = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
    )
}