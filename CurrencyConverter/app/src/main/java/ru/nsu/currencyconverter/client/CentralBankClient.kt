package ru.nsu.currencyconverter.client

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nsu.currencyconverter.api.CurrencyApiService
import ru.nsu.currencyconverter.model.Currency
import java.util.concurrent.TimeUnit

class CentralBankClient {
    private val api: CurrencyApiService
    private var currencyList: List<Currency> = emptyList()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.cbr-xml-daily.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build()
            )
            .build()
        api = retrofit.create(CurrencyApiService::class.java)
    }

    suspend fun loadCurrencies(
        onSuccess: (List<Currency>) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("CentralBankClient", "Начало загрузки валют")
        try {
            val response = withContext(Dispatchers.IO) { api.getCurrencies() }
            if (response.isSuccessful && response.body() != null) {
                currencyList = response.body()!!.Valute.values.toMutableList().apply {
                    add(0, Currency("RUB", "643", "RUB", 1, "Российский рубль", 1.0, 1.0))
                }
                Log.d("CentralBankClient", "Валюты успешно загружены")
                onSuccess(currencyList)
            } else {
                val errorMsg = "Ошибка: ${response.message()}"
                Log.e("CentralBankClient", errorMsg)
                onError(errorMsg)
            }
        } catch (e: Exception) {
            val errorMsg = "Ошибка сети: ${e.localizedMessage}"
            Log.e("CentralBankClient", errorMsg)
            onError(errorMsg)
        } finally {
            Log.d("CentralBankClient", "Загрузка валют завершена")
        }
    }

    fun getCurrencyList(): List<Currency> = currencyList
}
