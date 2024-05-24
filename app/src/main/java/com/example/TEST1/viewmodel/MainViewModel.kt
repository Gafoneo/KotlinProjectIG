package com.example.TEST1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.TEST1.model.Amount
import com.example.TEST1.model.CbrResult
import com.example.TEST1.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class MainViewModel(private val repository: Repository) : ViewModel() {

    val currencies   = MutableStateFlow(HashMap<String, CbrResult.Valuta>())
    val displayState = MutableStateFlow(DisplayState())

    init {
        setPeriod(Period.LastWeek)

        viewModelScope.launch(Dispatchers.IO) {
            repository.getDaily().collect {
                currencies.value = it?.valute ?: HashMap()
            }
        }
    }

    data class DisplayState(
        val transactions: Flow<List<Transaction>> = MutableStateFlow(listOf()),
        val totalIncome: Flow<Amount> = MutableStateFlow(Amount()),
        val totalExpenses: Flow<Amount> = MutableStateFlow(Amount())
    )

    enum class Period {
        LastWeek,
        LastMonth,
        AllTime;

        fun getDisplayName() = when (this) {
            LastWeek -> "Last week"
            LastMonth -> "Last month"
            AllTime -> "All time"
        }
    }

    fun setPeriod(period: Period) {
        val currentDate = Date().time
        val date = when (period) {
            Period.LastWeek -> Date(currentDate - 7 * 24 * 60 * 60 * 1000L)
            Period.LastMonth -> Date(currentDate - 30 * 24 * 60 * 60 * 1000L)
            Period.AllTime -> Date(0)
        }

        displayState.value = DisplayState(
            repository.getTransactionForDate(date),
            repository.getTotalIncomeAmount(date),
            repository.getTotalExpensesAmount(date)
        )
    }

    fun delete(transaction: Transaction) {
        viewModelScope.launch { repository.delete(transaction) }
    }

    fun upsert(transaction: Transaction) {
        viewModelScope.launch {
            repository.upsert(transaction)
        }
    }
}