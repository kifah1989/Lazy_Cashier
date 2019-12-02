package com.example.lazycashier

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Moshi
import de.tobiasschuerg.money.Currency
import de.tobiasschuerg.money.Money
import kotlinx.android.synthetic.main.main_activity.*
import okhttp3.*
import java.io.IOException
import java.lang.Thread.sleep
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set


class MainActivity : AppCompatActivity() {
    var valueList: ArrayList<Double> = arrayListOf()
    var keyList: ArrayList<String> = arrayListOf()
    val currencyRates: HashMap<String, Double> = HashMap()
    var code = "AED"
    var code2: String = "AED"
    var rate: Double = 0.0
    var rate2: Double = 0.0
    var cur1 = Currency(code, code, rate)
    var cur2 = Currency(code2, code2, rate2)
    var mon = Money(1, cur1)
    var mon2 = Money(1, cur2)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        run()
        sleep(2000)
        keyList = ArrayList(currencyRates.keys.sorted())
        valueList = ArrayList(currencyRates.values.sorted())

        //start spinner adapter and onitemselect listener
        val adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_dropdown_item, // Layout
            keyList // Array
        )

        // Set the drop down view resource
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Finally, data bind the spinner object with dapter
        spinner.adapter = adapter
        spinner2.adapter = adapter

        // Set an on item selected listener for spinner object
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                code = spinner.selectedItem.toString()
                rate = currencyRates[code]!!
                cur1 = Currency(code, code, rate)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                code2 = spinner2.selectedItem.toString()
                rate2 = currencyRates[code2]!!
                cur2 = Currency(code2, code2, rate2)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        //spinner todo
        //code = mSpinner.selectedItem.toString()
        //rate = currencyRates[code]!!
        //finish spinner adapter and onitemselect listener
    }

    fun click(view: View) {
        if (validate())
            calculate()
    }

    @SuppressLint("SetTextI18n")
    private fun validate(): Boolean {
        when {
            editText.text.isEmpty() -> {
                editText.error = "please enter amount"
                return false
            }
            editText2.text.isEmpty() -> {
                editText2.error = "please enter amount"
                return false
            }
            editText.text.length >= 6 -> {
                editText.error = "WoOo you have lots of money"
                return false
            }
            else -> {
                return true
            }
        }
    }
    fun calculate() {
        cur1 = Currency(code, code, rate)

        cur2 = Currency(code2, code2, rate2)
        val iHave1 = editText.text.toString().toDouble()
        val itemPrice1 = editText2.text.toString().toDouble()
        val ihaveMoney = Money(iHave1, cur1)
        val itemMoney = Money(itemPrice1, cur2)
        val ihavetoitem = ihaveMoney.convertInto(cur2)
        val itemtoihave = itemMoney.convertInto(cur1)

        val returnMoney1 = ihaveMoney - itemtoihave
        val returnMoney2 = ihavetoitem - itemMoney




        when {
            ihaveMoney.amount.toDouble() < itemtoihave.amount.toDouble() -> {
                editText.error = "you dont have enough money"
                textView.text = ""

            }
            returnMoney1.amount.toInt() == returnMoney2.amount.toInt() -> {
                textView.text = ihaveMoney.toString() + " is equal to " + itemMoney.toString()
            }
            else -> {
                editText.error = null
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
                textView5.text = "= " + ihaveMoney.convertInto(cur2)
                textView6.text = "= " + itemMoney.convertInto(cur1)
            }
        }
    }
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().build()
    private val ratesJsonAdapter = moshi.adapter(fixer::class.java)
    @Throws(Exception::class)
    fun run() {
        val request = Request.Builder()
            .url("http://data.fixer.io/api/latest?access_key=997d4f2093f733edadf912c7918d8a84&symbols=USD,AED,EUR,LBP,AUD.BHD,EGP,GBP,QAR,SAR,SYP,SAR,INR")
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
                        currencyRates[k] = v
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


