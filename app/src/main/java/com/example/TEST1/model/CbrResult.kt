package com.example.TEST1.model

import com.google.gson.annotations.SerializedName

data class CbrResult(
    @SerializedName("Valute")
    val valute: HashMap<String, Valuta>
) {
    class Valuta(
        @SerializedName("Value")
        val value: String
    )
}
