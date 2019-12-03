package com.example.lazycashier

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
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
    var valueList: ArrayList<Double> = arrayListOf()
    var keyList: ArrayList<String> = arrayListOf()
    val currencyRates: HashMap<String, Double> = HashMap()
    var code = "USD"
    var code2: String = "USD"
    var rate: Double = 0.0
    var rate2: Double = 0.0
    var cur1 = Currency(code, code, rate)
    var cur2 = Currency(code2, code2, rate2)

    lateinit var spinnerTitles: Array<String>
    lateinit var spinnerImages: IntArray
    var mSpinner: Spinner? = null
    var mSpinner2: Spinner? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        run()
        sleep(2000)
        keyList = ArrayList(currencyRates.keys.sorted())
        valueList = ArrayList(currencyRates.values.sorted())
        //
        val keys = arrayListOf(
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
        //valueList = arrayListOf(currencyRates["USD"]!! ,currencyRates["LBP"]!! ,currencyRates["SYP"]!! ,currencyRates["EUR"]!! ,currencyRates["AED"]!! ,currencyRates["SAR"]!! ,currencyRates["QAR"]!! ,currencyRates["INR"]!! ,currencyRates["AUD"]!! ,currencyRates["GBP"]!! ,currencyRates["EGP"]!! ,currencyRates["BHD"]!!)

        //USD,AED,EUR,LBP,AUD.BHD,EGP,GBP,QAR,SAR,SYP,INR

        mSpinner = findViewById<View>(R.id.spinner) as Spinner
        mSpinner2 = findViewById<View>(R.id.spinner2) as Spinner

        spinnerTitles = arrayOf(
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
        spinnerImages = intArrayOf(
            R.drawable.flag_usd
            , R.drawable.flag_lbp
            , R.drawable.flag_syp
            , R.drawable.flag_eur
            , R.drawable.flag_aed
            , R.drawable.flag_sar
            , R.drawable.flag_qar
            , R.drawable.flag_inr
            , R.drawable.flag_aud
            , R.drawable.flag_gbp
            , R.drawable.flag_egp
            , R.drawable.flag_bhd
        )
        val mCustomAdapter = SpinnerAdapter(this@MainActivity, spinnerTitles, spinnerImages)
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
                code = keys[position]
                rate = currencyRates[code]!!
                cur1 = Currency(code, code, rate)
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
                code2 = keys[position]
                rate2 = currencyRates[code2]!!
                cur2 = Currency(code2, code2, rate2)

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
            editText.text.length >= 7 -> {
                editText.error = "WoOo you have lots of money"
                textView.text = ""
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

        textView5.text = "= " + ihavetoitem
        textView6.text = "= " + itemtoihave
        when {
            ihaveMoney.amount.toDouble() < itemtoihave.amount.toDouble() -> {
                editText.error = "you dont have enough money"
                textView.text = ""
                textView2.text = ""

            }
            returnMoney1.amount.toInt() == returnMoney2.amount.toInt() -> {
                textView2.text = ""
                textView.text = ihaveMoney.toString() + " is equal to " + itemMoney.toString()
            }
            else -> {
                editText.error = null
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

    @JsonClass(generateAdapter = true)
    data class fixer(
        val base: String,
        val date: String,
        val rates: Map<String, Double>,
        val success: Boolean,
        val timestamp: Int
    )
}


