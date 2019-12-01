package com.example.lazycashier

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


class ParseResponseWithMoshi {
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().build()
    private val ratesJsonAdapter = moshi.adapter(fixer::class.java)
    @Throws(Exception::class)
    fun run() {
        val request = Request.Builder()
            .url("http://data.fixer.io/api/latest?access_key=997d4f2093f733edadf912c7918d8a84")
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val getCurRates = ratesJsonAdapter.fromJson(response.body!!.source())
            val cur = getCurRates!!.rates
            for ((k, v) in cur) {
                println("$k $v")
            }
        }
    }

    data class fixer(
        val base: String,
        val date: String,
        val rates: Map<String, Double>,
        val success: Boolean,
        val timestamp: Int
    )


    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            ParseResponseWithMoshi().run()
        }
    }
}
