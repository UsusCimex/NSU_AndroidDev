package ru.nsu.currencyconverter.ui

import ru.nsu.currencyconverter.model.Currency

interface CurrencySelectionListener {
    fun onCurrencySelected(currency: Currency)
}