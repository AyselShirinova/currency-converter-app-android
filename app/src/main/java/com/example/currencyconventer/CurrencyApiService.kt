package com.example.currencyconventer

import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiService {
    @GET("v2/rate/{base}/{quote}")
    suspend fun getExchangeRate(
        @Path("base") base: String,
        @Path("quote") quote: String
    ): ExchangeRateResponse
}