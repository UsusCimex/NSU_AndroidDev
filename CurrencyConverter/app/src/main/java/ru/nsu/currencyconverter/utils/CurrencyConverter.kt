package ru.nsu.currencyconverter.utils

import ru.nsu.currencyconverter.model.Currency

class CurrencyConverter {

    fun convert(amount: Double, fromCurrency: Currency, toCurrency: Currency): Double {
        return amount * fromCurrency.Value / toCurrency.Value
    }
}
