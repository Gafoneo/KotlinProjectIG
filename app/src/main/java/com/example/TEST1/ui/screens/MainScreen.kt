package com.example.TEST1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TEST1.Utils
import com.example.TEST1.model.Amount
import com.example.TEST1.model.Transaction
import com.example.TEST1.ui.theme.LightLime
import com.example.TEST1.ui.theme.LightRed
import com.example.TEST1.ui.theme.income
import com.example.TEST1.ui.theme.Lime
import com.example.TEST1.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.round

@Composable
fun MainScreen(viewModel: MainViewModel, addOrEdit: (Transaction?) -> Unit) {

    val displayState = viewModel.displayState.collectAsState().value
    val transactions = displayState.transactions.collectAsState(listOf())
    val totalIncome = displayState.totalIncome.collectAsState(Amount())
    val totalExpenses = displayState.totalExpenses.collectAsState(Amount())

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { addOrEdit(null) },
                    modifier = Modifier
                        .offset(y = (-29).dp)
                ) {
                    Icon(Icons.Default.Add, "Add")
                }
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Spacer(Modifier.width(16.dp))
                    for (period in MainViewModel.Period.entries) {
                        Button(
                            modifier = Modifier
                                .width(110.dp)
                                .height(30.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Lime),
                            onClick = { viewModel.setPeriod(period) },
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    fontSize = 12.sp,
                                    modifier = Modifier.background(color = Lime),
                                    text = period.getDisplayName(),
                                    color = Color.White
                                )
                            }

                        }
                        Spacer(Modifier.width(16.dp))
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    TotalItem(true, totalIncome.value.value)
                    Spacer(Modifier.width(64.dp))
                    TotalItem(false, totalExpenses.value.value)
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(transactions.value) { note ->
                        TransactionItem(note, viewModel, addOrEdit)
                    }
                }
            }


        }
        Row(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 9.dp)) {
            val currencies by viewModel.currencies.collectAsState()
            val currencyCodes = listOf("USD", "EUR", "GBP")
            for (code in currencyCodes) {
                currencies[code]?.let {

                    Box(modifier = Modifier
                            .padding(
                                start = 15.dp,
                            )
                    ) {
                        Text(
                            fontSize = 18.sp,
                            modifier = Modifier
                                .background(
                                    color = Lime,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(2.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            text = "$code: ${round((it.value).toFloat() * 10.0.pow(2)) / 10.0.pow(2)}",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TotalItem(
    isIncome: Boolean,
    value: Double
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val color = if (isIncome) income else Color.Red
        Text(
            fontSize = 24.sp,
            modifier = Modifier,
            text = if (isIncome) "Income" else "Expenses",
            color = color
        )
        Text(
            fontSize = 32.sp,
            modifier = Modifier,
            text = Utils.formatAmount(value.absoluteValue),
            color = color
        )
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    viewModel: MainViewModel,
    edit: (Transaction?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (transaction.amount > 0) LightLime else LightRed)
            .clickable {
                edit(transaction)
            }
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.name, style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = (if (transaction.amount > 0) "+" else "-") + Utils.formatAmount(transaction.amount.absoluteValue), style = MaterialTheme.typography.bodyMedium)

            val format = SimpleDateFormat.getDateTimeInstance()
            Text(format.format(transaction.date))
        }

        IconButton(onClick = { viewModel.delete(transaction) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}