package com.example.formula1.util

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

object AgeCalculator {
    // dd/MM/yyyy é o formato confirmado da API; ISO fica como fallback caso a API mude o formato.
    private val formatters = listOf(
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ISO_LOCAL_DATE
    )

    /** Recebe "dd/MM/yyyy" (ou ISO "yyyy-MM-dd"). Retorna idade em anos, ou null se inválido. */
    fun ageFromBirthday(birthday: String, today: LocalDate = LocalDate.now()): Int? {
        val trimmed = birthday.trim()
        for (formatter in formatters) {
            try {
                val birth = LocalDate.parse(trimmed, formatter)
                return if (birth.isAfter(today)) null else Period.between(birth, today).years
            } catch (e: Exception) {
                // tenta o próximo formato
            }
        }
        return null
    }
}
