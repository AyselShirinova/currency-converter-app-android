package com.example.currencyconventer

data class ExchangeRateResponse(
    val date: String,
    val base: String,
    val quote: String,
    val rate: Double
)