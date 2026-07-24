package com.example.currencyconventer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.frankfurter.dev/")
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .build()

    val api: CurrencyApiService =
        retrofit.create(CurrencyApiService::class.java)
}