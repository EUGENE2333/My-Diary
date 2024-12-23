package com.example.mydiary.data.utils

fun String?.isNotNullOrEmpty() = !isNullOrEmpty()

fun String?.isNotNullOrBlank() = !isNullOrBlank()

/**
 * Removes the currency symbol from a string.
 */
fun String.removeCurrencySymbol(): String = replace(Regex("[^\\d.]"), "").trim()

/**
 * Get the currency symbol from a string.
 */
fun String.extractCurrency(): String? = Regex("[^\\d.]").find(this)?.value?.trim()