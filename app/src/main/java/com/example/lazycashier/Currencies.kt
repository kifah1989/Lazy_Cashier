package com.example.lazycashier

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Currencies {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("rates")
    @Expose
    var rates: HashMap<String, Double> = HashMap()
}










