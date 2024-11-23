package ru.nsu.currencyconverter.utils

import android.util.Log
import ru.nsu.currencyconverter.model.Currency

class CurrencyConverter {
    fun convert(amount: Double, fromCurrency: Currency, toCurrency: Currency): Double {
        val result = amount * fromCurrency.Value / toCurrency.Value
        Log.d(
            "CurrencyConverter",
            "Конвертация $amount ${fromCurrency.CharCode} в ${toCurrency.CharCode}: $result"
        )
        return result
    }
}
