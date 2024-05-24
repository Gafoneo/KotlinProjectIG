package com.example.TEST1

import java.text.DecimalFormat

object Utils {
    fun formatAmount(double: Double): String {
        return DecimalFormat("$0.00").format(double)
    }
}