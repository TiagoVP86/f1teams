package com.example.formula1.util

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

object AgeCalculator {
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    /** Recebe "dd/MM/yyyy". Retorna idade em anos, ou null se inválido. */
    fun ageFromBirthday(birthday: String, today: LocalDate = LocalDate.now()): Int? =
        try {
            val birth = LocalDate.parse(birthday.trim(), formatter)
            if (birth.isAfter(today)) null else Period.between(birth, today).years
        } catch (e: Exception) {
            null
        }
}
