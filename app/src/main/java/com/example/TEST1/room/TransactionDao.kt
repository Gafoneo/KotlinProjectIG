package com.example.TEST1.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.TEST1.model.Amount
import com.example.TEST1.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionDao {
    @Upsert
    suspend fun upsert(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT * FROM `Transaction` WHERE date > :date")
    fun getTransactionForDate(date: Date): Flow<List<Transaction>>

    @Query("SELECT sum(amount) as value FROM `Transaction` WHERE date > :date AND amount > 0")
    fun getTotalIncomeAmount(date: Date): Flow<Amount>

    @Query("SELECT sum(amount) as value FROM `Transaction` WHERE date > :date AND amount < 0")
    fun getTotalExpensesAmount(date: Date): Flow<Amount>
}