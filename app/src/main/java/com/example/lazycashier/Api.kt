package com.example.lazycashier

import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("http://data.fixer.io/api/latest?access_key=997d4f2093f733edadf912c7918d8a84&symbols=USD,LBP,SYP,EUR,AED,SAR,QAR,AUD,GBP,EGP,BHD")
    fun getCurrencies(
    ): Call<Currencies>
}