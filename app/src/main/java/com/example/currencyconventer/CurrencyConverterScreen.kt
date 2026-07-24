package com.example.currencyconventer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun CurrencyConverterScreen(modifier: Modifier = Modifier) {

    var userInput by remember { mutableStateOf("")}
    var fromCurrency by remember { mutableStateOf("")}
    var toCurrency by remember { mutableStateOf("")}
    var result by remember { mutableStateOf("")}
    val currencyList = listOf( "USD", "EUR", "AZN", "TRY", "RUB")
    val scope = rememberCoroutineScope()
    var exchangeRate by remember { mutableStateOf<Double?>(null)}

    fun updateResult( input: String, from: String, to: String) {

        val amount = input.toDoubleOrNull()

        if (amount != null && from.isNotEmpty() && to.isNotEmpty()) {
            scope.launch {
                try {
                    val response = RetrofitInstance.api.getExchangeRate(
                        base = from, quote = to
                    )
                    exchangeRate = response.rate
                    val convertedAmount = amount * response.rate
                    result = "%.2f".format(convertedAmount)
                } catch (e: Exception) {
                    result = "Error"
                }
            }
        } else {
            result = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        // ------Input row--------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = { input ->
                    userInput = input
                    updateResult(
                        input = input, from = fromCurrency, to = toCurrency
                    )
                },
                modifier = Modifier.weight(1f),
                label = {
                    Text("Amount")
                },
                singleLine = true
            )

            CurrencyDropdown(
                selectedCurrency = fromCurrency,
                currencies = currencyList,

                onCurrencySelected = { selectedCurrency ->
                    fromCurrency = selectedCurrency
                    updateResult(
                        input = userInput, from = selectedCurrency, to = toCurrency
                    )
                }
            )
        }

        //space between rows
        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Swap button
        Button(
            onClick = {
                //swap variables
                val temp = fromCurrency
                fromCurrency = toCurrency
                toCurrency = temp

                updateResult(
                    input = userInput,
                    from = toCurrency,
                    to = fromCurrency
                )
            },
            modifier = Modifier.size(56.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("⇅")
        }

        //space
        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Output row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = result,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp
                )
            }

            CurrencyDropdown(
                selectedCurrency = toCurrency,
                currencies = currencyList,

                onCurrencySelected = { selectedCurrency ->
                    toCurrency = selectedCurrency

                    updateResult(
                        input = userInput,
                        from = fromCurrency,
                        to = selectedCurrency
                    )
                }
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // chart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(12.dp)
                ),

            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Exchange Rate Chart",
                fontSize = 18.sp
            )
        }
    }
}

// currency dropdown
@Composable
fun CurrencyDropdown(
    selectedCurrency: String,
    currencies: List<String>,
    onCurrencySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false)}

    Box {
        Button(
            onClick = {
                expanded = true
            })
        {
            Text(
                text = if (selectedCurrency.isEmpty()) {
                    "Select"}
                else{
                    selectedCurrency
                }
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            })
        {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = {
                        Text(currency)
                    },
                    onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview( showBackground = true)
@Composable
fun Preview() {
    CurrencyConverterScreen()
}