package ru.nsu.currencyconverter.api

import retrofit2.Response
import retrofit2.http.GET
import ru.nsu.currencyconverter.model.CurrencyResponse

interface CurrencyApiService {
    @GET("daily_json.js")
    suspend fun getCurrencies(): Response<CurrencyResponse>
}