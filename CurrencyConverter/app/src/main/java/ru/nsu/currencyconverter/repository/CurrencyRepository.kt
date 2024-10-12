package ru.nsu.currencyconverter.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nsu.currencyconverter.api.CurrencyApiService
import ru.nsu.currencyconverter.model.CurrencyResponse

class CurrencyRepository {

    private val api: CurrencyApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.cbr-xml-daily.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(CurrencyApiService::class.java)
    }

    suspend fun getCurrencies(): CurrencyResponse? {
        val response = api.getCurrencies()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}