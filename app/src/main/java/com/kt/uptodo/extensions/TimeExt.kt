package com.kt.uptodo.extensions

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

fun OffsetDateTime.parse(): String {
    return this.format(formatter)
}