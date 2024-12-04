package ru.nsu.currencyconverter.logic

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.launch
import ru.nsu.currencyconverter.R
import ru.nsu.currencyconverter.client.CentralBankClient
import ru.nsu.currencyconverter.databinding.ActivityMainBinding
import ru.nsu.currencyconverter.fragment.LoadingDialogFragment
import ru.nsu.currencyconverter.fragment.RetryDialogFragment
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
    private var loadingDialog: LoadingDialogFragment? = null
    var selectedFromCurrency: Currency? = null
    var selectedToCurrency: Currency? = null

    interface MainLogicCallback {
        fun onCurrenciesLoaded()
        fun onCurrenciesFailed(errorMessage: String)
        fun onConversionResult(result: String)
    }

    fun loadCurrencies() {
        showLoadingDialog()
        Log.d("MainLogic", "Начало загрузки валют")

        lifecycleScope.launch {
            client.loadCurrencies(
                onSuccess = {
                    hideLoadingDialog()
                    callback.onCurrenciesLoaded()
                    Log.d("MainLogic", "Валюты успешно загружены")
                },
                onError = { errorMessage ->
                    hideLoadingDialog()
                    Log.e("MainLogic", "Ошибка загрузки валют: $errorMessage")
                    callback.onCurrenciesFailed(errorMessage)
                }
            )
        }
    }

    private fun showLoadingDialog(message: String = "Загрузка...") {
        if (loadingDialog?.isVisible == true) return
        loadingDialog = LoadingDialogFragment.newInstance(message)
        loadingDialog?.show((context as AppCompatActivity).supportFragmentManager, "LoadingDialog")
        Log.d("MainLogic", "Показан диалог загрузки")
    }

    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
        Log.d("MainLogic", "Диалог загрузки скрыт")
    }

    fun retryLoadCurrencies() {
        Toast.makeText(context, "Повторная загрузка данных...", Toast.LENGTH_SHORT).show()
        Log.d("MainLogic", "Повторная попытка загрузки валют")
        loadCurrencies()
    }

    fun convertCurrency(amount: Double?) {
        if (selectedFromCurrency == null || selectedToCurrency == null || amount == null) {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
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
        val retryDialog = RetryDialogFragment.newInstance("$errorMessage. Попробовать снова?")
        retryDialog.setRetryDialogListener(object : RetryDialogFragment.RetryDialogListener {
            override fun onRetry() {
                Log.d("MainLogic", "Пользователь выбрал повторную загрузку")
                retryLoadCurrencies()
            }

            override fun onExit() {
                Log.d("MainLogic", "Пользователь выбрал выход")
                (context as AppCompatActivity).finish()
            }
        })
        retryDialog.show((context as AppCompatActivity).supportFragmentManager, "RetryDialog")
    }

    fun handleCurrencySelection(
        currency: Currency,
        type: CurrencyType,
        binding: ActivityMainBinding
    ) {
        when (type) {
            CurrencyType.FROM -> {
                selectedFromCurrency = currency
                binding.buttonSelectFromCurrency.text = currency.Name
                Log.d("MainLogic", "Выбрана валюта исходная: ${currency.Name}")
            }

            CurrencyType.TO -> {
                selectedToCurrency = currency
                binding.buttonSelectToCurrency.text = currency.Name
                Log.d("MainLogic", "Выбрана валюта целевая: ${currency.Name}")
            }
        }
    }
}
