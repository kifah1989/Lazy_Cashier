package com.example.lazycashier

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lazycashier.R.drawable.*
import com.squareup.moshi.JsonClass
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
    val currencyRates: HashMap<String, Double> = HashMap()
    var codeList: ArrayList<String> = arrayListOf()
    var currencyName: ArrayList<String> = arrayListOf()
    var valueList: ArrayList<Double> = arrayListOf()
    var currencyList: ArrayList<Currency> = arrayListOf()
    var currencyListNames: HashMap<String, Currency> = hashMapOf()
    var flags: ArrayList<Int> = arrayListOf()
    lateinit var iHaveCurrency: Currency
    lateinit var itemCurrency: Currency


    var mSpinner: Spinner? = null
    var mSpinner2: Spinner? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        run()
        sleep(2000)
        codeList = arrayListOf(
            "USD",
            "LBP",
            "SYP",
            "EUR",
            "AED",
            "SAR",
            "QAR",
            "INR",
            "AUD",
            "GBP",
            "EGP",
            "BHD"
        )
        currencyName = arrayListOf(
            "Us Dollar",
            "ليرة لبنانية",
            "ليرة سورية",
            "Euro",
            "درهم امراتي",
            "ريال سعودي",
            "ريال قطري",
            "Indian Rupee",
            "Australian Dollar",
            "GBP",
            "جنه مصري",
            "دينار بحرين"
        )
        flags = arrayListOf(
            flag_usd,
            flag_lbp,
            flag_syp,
            flag_eur,
            flag_aed,
            flag_sar,
            flag_qar,
            flag_inr,
            flag_aud,
            flag_gbp,
            flag_egp,
            flag_bhd
        )
        for (key in codeList) {
            valueList.add(currencyRates[key]!!)
        }
        for (i in 0..2)
            currencyList.add(Currency(codeList[i], currencyName[i], valueList[i]))



        //USD,AED,EUR,LBP,AUD.BHD,EGP,GBP,QAR,SAR,SYP,INR

        mSpinner = findViewById<View>(R.id.spinner) as Spinner
        mSpinner2 = findViewById<View>(R.id.spinner2) as Spinner

        val mCustomAdapter = SpinnerAdapter(this@MainActivity, currencyName, flags)
        mSpinner!!.adapter = mCustomAdapter
        mSpinner2!!.adapter = mCustomAdapter

        mSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // Display the selected item text on text view
                iHaveCurrency = currencyList[position]
                val rate = valueList[position]
                textView3.text = "Amount you have in ${currencyName[position]}"
                if (rate >= 1000)
                    editText.filters = arrayOf<InputFilter>(LengthFilter(6))
                else
                    editText.filters = arrayOf<InputFilter>(LengthFilter(3))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        mSpinner2!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // Display the selected item text on text view
                itemCurrency = currencyList[position]
                val rate2 = valueList[position]
                textView4.text = "Item Price in ${currencyName[position]}"
                if (rate2 >= 1000)
                    editText2.filters = arrayOf<InputFilter>(LengthFilter(6))
                else
                    editText2.filters = arrayOf<InputFilter>(LengthFilter(4))

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
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
                textView.text = ""
                textView2.text = ""
                textView5.text = ""
                textView6.text = ""

                return false
            }
            editText2.text.isEmpty() -> {
                editText2.error = "please enter amount"
                textView2.text = ""
                textView5.text = ""
                textView6.text = ""
                return false
            }
            else -> {
                return true
            }
        }
    }
    fun calculate() {
        val ihave = editText.text.toString().toDouble()
        val itemPrice = editText2.text.toString().toDouble()
        val iHaveMoney = Money(ihave, iHaveCurrency)
        val itemMoney = Money(itemPrice, itemCurrency)

        val ihavetoitem = iHaveMoney.convertInto(itemCurrency)
        val itemtoihave = itemMoney.convertInto(iHaveCurrency)

        val returnMoney1 = iHaveMoney - itemtoihave
        val returnMoney2 = ihavetoitem - itemMoney

        textView5.text = "= " + ihavetoitem
        textView6.text = "= " + itemtoihave
        when {
            iHaveMoney.amount.toDouble() < itemtoihave.amount.toDouble() -> {
                editText.error = "you dont have enough money"
                textView.text = ""
                textView2.text = ""

            }
            returnMoney1.amount.toInt() == returnMoney2.amount.toInt() -> {
                textView2.text = ""
                textView.text = iHaveMoney.toString() + " is equal to " + itemMoney.toString()
            }
            else -> {
                editText.error = null
                textView.text = "return: $returnMoney1"
                textView2.text = "return: $returnMoney2"
            }
        }
        val programmingList = findViewById<RecyclerView>(R.id.recyclerView)
        programmingList.layoutManager = LinearLayoutManager(this)

        programmingList.adapter = CurrencyAdapter(valueList, flags)
    }
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().build()
    private val ratesJsonAdapter = moshi.adapter(fixer::class.java)
    @Throws(Exception::class)
    fun run() {
        val request = Request.Builder()
            .url("http://data.fixer.io/api/latest?access_key=997d4f2093f733edadf912c7918d8a84&symbols=USD,LBP,SYP,EUR,AED,SAR,QAR,INR,AUD,GBP,EGP,BHD")
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

    @JsonClass(generateAdapter = true)
    data class fixer(
        val base: String,
        val date: String,
        val rates: Map<String, Double>,
        val success: Boolean,
        val timestamp: Int
    )
}


