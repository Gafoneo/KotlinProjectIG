package com.example.TEST1.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.TEST1.model.Transaction

@Database(entities = [Transaction::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val transactionDao: TransactionDao
}