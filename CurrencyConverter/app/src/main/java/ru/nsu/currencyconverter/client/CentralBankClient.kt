package ru.nsu.currencyconverter.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nsu.currencyconverter.api.CurrencyApiService
import ru.nsu.currencyconverter.model.Currency

class CentralBankClient {

    private val api: CurrencyApiService
    private var currencyList: List<Currency> = emptyList()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.cbr-xml-daily.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(CurrencyApiService::class.java)
    }

    suspend fun loadCurrencies(
        onSuccess: (List<Currency>) -> Unit,
        onError: (String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.getCurrencies()
                if (response.isSuccessful && response.body() != null) {
                    currencyList = response.body()!!.Valute.values.toMutableList().apply {
                        add(0, Currency("RUB", "643", "RUB", 1, "Российский рубль", 1.0, 1.0))
                    }
                    onSuccess(currencyList)
                } else {
                    onError("Ошибка: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Ошибка сети: ${e.localizedMessage}")
            }
        }
    }

    fun getCurrencyList(): List<Currency> {
        return currencyList
    }
}
