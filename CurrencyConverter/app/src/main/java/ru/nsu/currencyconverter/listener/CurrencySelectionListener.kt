package ru.nsu.currencyconverter.listener

import ru.nsu.currencyconverter.model.Currency
import ru.nsu.currencyconverter.model.CurrencyType

interface CurrencySelectionListener {
    fun onCurrencySelected(currency: Currency, type: CurrencyType)
}
