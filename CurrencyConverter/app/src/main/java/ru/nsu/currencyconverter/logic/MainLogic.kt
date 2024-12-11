package ru.nsu.currencyconverter.logic

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.launch
import ru.nsu.currencyconverter.R
import ru.nsu.currencyconverter.client.CentralBankClient
import ru.nsu.currencyconverter.model.Currency
import ru.nsu.currencyconverter.model.CurrencyType
import ru.nsu.currencyconverter.utils.CurrencyConverter

class MainLogic(
    private val context: Context,
    private val lifecycleScope: LifecycleCoroutineScope,
    val client: CentralBankClient,
    private val converter: CurrencyConverter,
    private val callback: MainLogicCallback
) {
    var selectedFromCurrency: Currency? = null
    var selectedToCurrency: Currency? = null

    interface MainLogicCallback {
        fun onCurrenciesLoaded()
        fun onCurrenciesFailed(errorMessage: String)
        fun onConversionResult(result: String)
        fun updateFromCurrencyText(name: String)
        fun updateToCurrencyText(name: String)
        fun showToast(message: String)
        fun finishActivity()
    }

    fun loadCurrencies() {
        Log.d("MainLogic", "Начало загрузки валют")

        lifecycleScope.launch {
            client.loadCurrencies(
                onSuccess = {
                    callback.onCurrenciesLoaded()
                    Log.d("MainLogic", "Валюты успешно загружены")
                },
                onError = { errorMessage ->
                    Log.e("MainLogic", "Ошибка загрузки валют: $errorMessage")
                    callback.onCurrenciesFailed(errorMessage)
                }
            )
        }
    }

    fun retryLoadCurrencies() {
        callback.showToast("Повторная загрузка данных...")
        Log.d("MainLogic", "Повторная попытка загрузки валют")
        loadCurrencies()
    }

    fun convertCurrency(amount: Double?) {
        if (selectedFromCurrency == null || selectedToCurrency == null || amount == null) {
            callback.showToast("Заполните все поля")
            Log.w("MainLogic", "Конвертация не выполнена: не все поля заполнены")
            return
        }

        val result = converter.convert(amount, selectedFromCurrency!!, selectedToCurrency!!)
        callback.onConversionResult(
            context.getString(
                R.string.conversion_result,
                amount,
                selectedFromCurrency!!.CharCode,
                result,
                selectedToCurrency!!.CharCode
            )
        )
        Log.d(
            "MainLogic",
            "Конвертировано $amount ${selectedFromCurrency!!.CharCode} в $result ${selectedToCurrency!!.CharCode}"
        )
    }

    fun showRetryDialog(errorMessage: String) {
        callback.showToast(errorMessage)
        callback.showToast("Повторите попытку или выйдите из приложения")
        Log.d("MainLogic", "Пользователь выбрал повторную загрузку")
        retryLoadCurrencies()
    }

    fun handleCurrencySelection(currency: Currency, type: CurrencyType) {
        when (type) {
            CurrencyType.FROM -> {
                selectedFromCurrency = currency
                callback.updateFromCurrencyText(currency.Name)
                Log.d("MainLogic", "Выбрана валюта исходная: ${currency.Name}")
            }
            CurrencyType.TO -> {
                selectedToCurrency = currency
                callback.updateToCurrencyText(currency.Name)
                Log.d("MainLogic", "Выбрана валюта целевая: ${currency.Name}")
            }
        }
    }

    fun shouldShowCurrencySelection(): Boolean {
        return !client.getCurrencyList().isEmpty()
    }

    fun getCurrencyList(): List<Currency> {
        return client.getCurrencyList()
    }
}
