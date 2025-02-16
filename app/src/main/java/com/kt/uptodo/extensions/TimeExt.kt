package com.kt.uptodo.extensions

import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
private val formatterMinute = DateTimeFormatter.ofPattern("HH:mm")

fun OffsetDateTime.parseMinute(): String {
    return this.format(formatterMinute)
}

fun OffsetDateTime.convertToDeadline(): String {
    val now = OffsetDateTime.now()

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