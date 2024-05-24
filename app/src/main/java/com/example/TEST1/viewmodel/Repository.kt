package com.example.TEST1.viewmodel

import com.example.TEST1.api.CbrService
import com.example.TEST1.model.Amount
import com.example.TEST1.model.CbrResult
import com.example.TEST1.model.Transaction
import com.example.TEST1.room.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

class Repository(private val db: AppDatabase) {

    fun getDaily(): Flow<CbrResult?> {
        return flow {
            emit(CbrService.getInstance().getDaily().body())
        }
    }

    fun getTransactionForDate(date: Date): Flow<List<Transaction>> {
        return db.transactionDao.getTransactionForDate(date)
    }

    fun getTotalIncomeAmount(date: Date): Flow<Amount> {
        return db.transactionDao.getTotalIncomeAmount(date)
    }

    fun getTotalExpensesAmount(date: Date): Flow<Amount> {
        return db.transactionDao.getTotalExpensesAmount(date)
    }

    suspend fun upsert(transaction: Transaction) {
        db.transactionDao.upsert(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        db.transactionDao.delete(transaction)
    }
}