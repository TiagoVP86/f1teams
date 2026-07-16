package com.example.formula1.util

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

object AgeCalculator {
    private val formatters = listOf(
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ISO_LOCAL_DATE
    )

    fun ageFromBirthday(birthday: String, today: LocalDate = LocalDate.now()): Int? {
        val trimmed = birthday.trim()
        for (formatter in formatters) {
            try {
                val birth = LocalDate.parse(trimmed, formatter)
                return if (birth.isAfter(today)) null else Period.between(birth, today).years
            } catch (e: Exception) {
                continue
            }
        }
        return null
    }
}
