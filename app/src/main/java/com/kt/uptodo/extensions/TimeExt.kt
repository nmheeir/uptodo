package com.kt.uptodo.extensions

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
private val formatterMinute = DateTimeFormatter.ofPattern("HH:mm")
private val formatterSecond = DateTimeFormatter.ofPattern("HH:mm:ss")

fun LocalDateTime.parseMinute(): String {
    return this.format(formatterMinute)
}

fun LocalTime.parse(): String {
    return this.format(formatterSecond)
}

fun LocalDateTime.convertToDeadline(): String {
    val now = LocalDateTime.now()

    val deadlineText = if (now.dayOfMonth == this.dayOfMonth) {
        "Today at " + this.parseMinute()
    } else {
        val duration = Duration.between(now, this)
        val minutesLeft = duration.toMinutes()
        val hoursLeft = duration.toHours()
        val daysLeft = duration.toDays()

        when {
            minutesLeft < 60 -> "Due in $minutesLeft minutes"
            hoursLeft < 24 -> "Due in $hoursLeft hours"
            daysLeft in 1..6 -> {
                val remainingHours = hoursLeft % 24
                "Due in $daysLeft days, $remainingHours hours"
            }

            else -> "Due in $daysLeft days"
        }
    }

    return deadlineText
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

fun DayOfWeek.displayText(uppercase: Boolean = false, narrow: Boolean = false): String {
    val style = if (narrow) TextStyle.NARROW else TextStyle.SHORT
    return getDisplayName(style, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

fun LocalTime.toSeconds(): Long {
    return (this.hour * 3600 + this.minute * 60 + this.second).toLong()
}

fun LocalTime.toMilliSeconds(): Long {
    return this.toSeconds() * 1000L
}