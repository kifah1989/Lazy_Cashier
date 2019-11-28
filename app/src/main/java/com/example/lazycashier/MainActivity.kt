package com.example.lazycashier

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Moshi
import de.tobiasschuerg.money.Currency
import de.tobiasschuerg.money.Money
import kotlinx.android.synthetic.main.main_activity.*
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    var cur: HashMap<String, Double> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        run()
    }

    fun click(view: View) {

        calculate()

    }

    private fun validate(): Boolean {
        val i_have = editText.text.toString()
        val item_price = editText2.text.toString()
        val euro = Currency("EUR", "Euro", 1.0)
        val lira = Currency("LBP", "Lebanese Pound", 1500.0)
        val aed = Currency("AED", "Arab Emirates Dirham", 4.0)
        val usd = Currency("USD", "US Dollars", 1.1)
        val ihaveMoney = Money(i_have.toDouble(), euro)
        val itemMoney = Money(item_price.toDouble(), euro)
        if (i_have.isEmpty()) {
            editText.error = "Field can't be empty"
        } else
        if (item_price.isEmpty()) {
            editText2.error = "Field can't be empty"
        } else
            if (spinner.selectedItem.toString() == "LBP" && i_have.length > 6) {
                editText.error = "WoOo you have lots of money"
            } else
                if (spinner2.selectedItem.toString() == "EUR") {
                    editText.error = "you broke"
                }
        return true
    }


    @SuppressLint("SetTextI18n")
    private fun calculate() {

        val i_have = editText.text.toString().toDouble()
        val item_price = editText2.text.toString().toDouble()
        val euro = Currency("EUR", "Euro", 1.0)
        val lira = Currency("LBP", "Lebanese Pound", 1500.0)
        val aed = Currency("AED", "Arab Emirates Dirham", 4.0)
        val usd = Currency("USD", "US Dollars", 1.1)


        when {
            spinner.selectedItem.toString() == "EUR" && spinner2.selectedItem.toString() == "EUR" -> {
                val ihaveMoney = Money(i_have, euro)
                val itemMoney = Money(item_price, euro)
                val con1 = ihaveMoney.convertInto(euro)
                val con2 = itemMoney.convertInto(euro)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"

            }
            spinner.selectedItem.toString() == "EUR" && spinner2.selectedItem.toString() == "LBP" -> {
                val ihaveMoney = Money(i_have, euro)
                val itemMoney = Money(item_price, lira)
                val con2 = itemMoney.convertInto(euro)
                val con1 = ihaveMoney.convertInto(lira)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "EUR" && spinner2.selectedItem.toString() == "AED" -> {
                val ihaveMoney = Money(i_have, euro)
                val itemMoney = Money(item_price, aed)
                val con2 = itemMoney.convertInto(euro)
                val con1 = ihaveMoney.convertInto(aed)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "EUR" && spinner2.selectedItem.toString() == "USD" -> {
                val ihaveMoney = Money(i_have, euro)
                val itemMoney = Money(item_price, usd)
                val con2 = itemMoney.convertInto(euro)
                val con1 = ihaveMoney.convertInto(usd)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "LBP" && spinner2.selectedItem.toString() == "EUR" -> {
                val ihaveMoney = Money(i_have, lira)
                val itemMoney = Money(item_price, euro)
                val con2 = itemMoney.convertInto(lira)
                val con1 = ihaveMoney.convertInto(euro)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "LBP" && spinner2.selectedItem.toString() == "LBP" -> {
                val ihaveMoney = Money(i_have, lira)
                val itemMoney = Money(item_price, lira)
                val con2 = itemMoney.convertInto(lira)
                val con1 = ihaveMoney.convertInto(lira)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "LBP" && spinner2.selectedItem.toString() == "AED" -> {
                val ihaveMoney = Money(i_have, lira)
                val itemMoney = Money(item_price, aed)
                val con2 = itemMoney.convertInto(lira)
                val con1 = ihaveMoney.convertInto(aed)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "LBP" && spinner2.selectedItem.toString() == "USD" -> {
                val ihaveMoney = Money(i_have, lira)
                val itemMoney = Money(item_price, usd)
                val con2 = itemMoney.convertInto(lira)
                val con1 = ihaveMoney.convertInto(usd)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "AED" && spinner2.selectedItem.toString() == "EUR" -> {
                val ihaveMoney = Money(i_have, aed)
                val itemMoney = Money(item_price, euro)
                val con2 = itemMoney.convertInto(aed)
                val con1 = ihaveMoney.convertInto(euro)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "AED" && spinner2.selectedItem.toString() == "LBP" -> {
                val ihaveMoney = Money(i_have, aed)
                val itemMoney = Money(item_price, lira)
                val con2 = itemMoney.convertInto(aed)
                val con1 = ihaveMoney.convertInto(lira)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "AED" && spinner2.selectedItem.toString() == "AED" -> {
                val ihaveMoney = Money(i_have, aed)
                val itemMoney = Money(item_price, aed)
                val con2 = itemMoney.convertInto(aed)
                val con1 = ihaveMoney.convertInto(aed)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "AED" && spinner2.selectedItem.toString() == "USD" -> {
                val ihaveMoney = Money(i_have, aed)
                val itemMoney = Money(item_price, usd)
                val con2 = itemMoney.convertInto(aed)
                val con1 = ihaveMoney.convertInto(usd)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "USD" && spinner2.selectedItem.toString() == "EUR" -> {
                val ihaveMoney = Money(i_have, usd)
                val itemMoney = Money(item_price, euro)
                val con2 = itemMoney.convertInto(usd)
                val con1 = ihaveMoney.convertInto(euro)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "USD" && spinner2.selectedItem.toString() == "LBP" -> {
                val ihaveMoney = Money(i_have, usd)
                val itemMoney = Money(item_price, lira)
                val con2 = itemMoney.convertInto(usd)
                val con1 = ihaveMoney.convertInto(lira)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "USD" && spinner2.selectedItem.toString() == "AED" -> {
                val ihaveMoney = Money(i_have, usd)
                val itemMoney = Money(item_price, aed)
                val con2 = itemMoney.convertInto(usd)
                val con1 = ihaveMoney.convertInto(aed)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
            spinner.selectedItem.toString() == "USD" && spinner2.selectedItem.toString() == "USD" -> {
                val ihaveMoney = Money(i_have, usd)
                val itemMoney = Money(item_price, usd)
                val con2 = itemMoney.convertInto(usd)
                val con1 = ihaveMoney.convertInto(usd)
                val returnMoney1 = ihaveMoney - con2
                val returnMoney2 = con1 - itemMoney
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
        }
    }

    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().build()
    private val ratesJsonAdapter = moshi.adapter(fixer::class.java)
    @Throws(Exception::class)
    fun run() {
        val request = Request.Builder()
            .url("http://data.fixer.io/api/latest?access_key=997d4f2093f733edadf912c7918d8a84&symbols=USD,AED,EUR,LBP")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val getCurRates = ratesJsonAdapter.fromJson(response.body!!.source())
                    val cur = getCurRates!!.rates
                    for ((k, v) in cur) {
                        println("$k $v")
                    }
                }
            }

        })

    }

    data class fixer(
        val base: String,
        val date: String,
        val rates: Map<String, Double>,
        val success: Boolean,
        val timestamp: Int
    )

}


