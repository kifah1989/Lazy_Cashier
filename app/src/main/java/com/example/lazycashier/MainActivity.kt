package com.example.lazycashier

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.InputFilter
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lazycashier.R.drawable.*
import de.tobiasschuerg.money.Currency
import de.tobiasschuerg.money.Money
import kotlinx.android.synthetic.main.main_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private val currencyRates: HashMap<String, Double> = HashMap()
    //val currencyRates: HashMap<String, Double> = hashMapOf()
    var valueList: ArrayList<Double> = ArrayList()
    var currencyList: ArrayList<Currency> = ArrayList()
    lateinit var iHaveCurrency: Currency
    lateinit var itemCurrency: Currency
    var mSpinner: Spinner? = null
    var mSpinner2: Spinner? = null
    var codeList: ArrayList<String> = arrayListOf()
    val moneyList = arrayListOf<Money>()
    var currencyName = arrayListOf(
        "درهم امراتي",
        "AU Dollar",
        "دينار بحرين",
        "جنه مصري",
        "Euro",
        "British P",
        "ليرة لبنانية",
        "ريال قطري",
        "ريال سعودي",
        "ليرة سورية",
        "US Dollar"
    )
    var flags = arrayListOf(
        flag_aed,
        flag_aud,
        flag_bhd,
        flag_egp,
        flag_eur,
        flag_gbp,
        flag_lbp,
        flag_qar,
        flag_sar,
        flag_syp,
        flag_usd
    )


    fun initialize() {
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
                    editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(6))
                else
                    editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(4))
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
                    editText2.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(6))
                else
                    editText2.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(4))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        getJason()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        getJason()


        //USD,AED,EUR,LBP,AUD.BHD,EGP,GBP,QAR,SAR,SYP,INR

    }
    fun click(view: View) {

            calculate()
    }



    fun calculate() {
        val currencyListview = findViewById<RecyclerView>(R.id.recyclerView)
        try {
            val ihave = editText.text.toString().toDouble()
            val itemPrice = editText2.text.toString().toDouble()
            val iHaveMoney = Money(ihave, iHaveCurrency)
            val itemMoney = Money(itemPrice, itemCurrency)
            val ihavetoitem = iHaveMoney.convertInto(itemCurrency)
            val itemtoihave = itemMoney.convertInto(iHaveCurrency)
            val returnMoney1 = iHaveMoney - itemtoihave
            val returnMoney2 = ihavetoitem - itemMoney
            for (currency in currencyList) {
                moneyList.add(Money(returnMoney1.convertInto(currency).amount, currency))
            }
            textView5.text = "= " + ihavetoitem
            textView6.text = "= " + itemtoihave
            when {
                iHaveMoney.amount.toDouble() < itemtoihave.amount.toDouble() -> {
                    editText.error = "not enough money"
                    textView5.text = ""
                    textView6.text = ""
                    moneyList.clear()
                    currencyListview.layoutManager = LinearLayoutManager(this@MainActivity)
                    currencyListview.adapter = CurrencyAdapter(moneyList, flags, currencyName)
                }
                returnMoney1.amount.toInt() == returnMoney2.amount.toInt() -> {
                    textView.text = iHaveMoney.toString() + " is equal to " + itemMoney.toString()
                    textView5.text = ""
                    textView6.text = ""
                    moneyList.clear()
                    currencyListview.layoutManager = LinearLayoutManager(this@MainActivity)
                    currencyListview.adapter = CurrencyAdapter(moneyList, flags, currencyName)

                }
                else -> {

                    currencyListview.layoutManager = LinearLayoutManager(this@MainActivity)

                    currencyListview.adapter = CurrencyAdapter(moneyList, flags, currencyName)
                }
            }
        } catch (ex: Exception) {
            moneyList.clear()
            currencyListview.layoutManager = LinearLayoutManager(this@MainActivity)
            currencyListview.adapter = CurrencyAdapter(moneyList, flags, currencyName)
            if (editText.text.isEmpty())
                editText.error = "please enter amount"
            if (editText2.text.isEmpty())
                editText2.error = "please enter amount"
            textView5.text = ""
            textView6.text = ""
        }
    }


    private fun getJason() {
        progressBar.visibility = View.VISIBLE
        ServiceGenerator.getApi().getCurrencies()
            .enqueue(object : Callback<Currencies> {
                override fun onResponse(call: Call<Currencies>, response: Response<Currencies>) {
                    Log.e(TAG, "log: -----------------------------")
                    Log.d(TAG, "onResponse: " + response.body())
                    if (response.raw().networkResponse() != null) {
                        Log.d(TAG, "onResponse: response is from NETWORK...")
                    } else if (response.raw().cacheResponse() != null
                        && response.raw().networkResponse() == null
                    ) {
                        Log.d(TAG, "onResponse: response is from CACHE...")
                    }

                    val currencies = response.body()
                    val timestamp = currencies!!.timestamp

                    val cal = Calendar.getInstance(Locale.ENGLISH)
                    cal.timeInMillis = timestamp!! * 1000
                    val date: String = DateFormat.format("dd-MM-yyyy @ hh:mm a", cal).toString()

                    textView2.text = "updated on " + date
                    val rates = currencies.rates.toSortedMap()

                    currencyRates.putAll(rates)

                    valueList.addAll(rates.values)

                    codeList.addAll(rates.keys)
                    for (i in 0..10)
                        currencyList.add(Currency(codeList[i], currencyName[i], valueList[i]))
                    initialize()
                    progressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<Currencies>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                    val builder: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(this@MainActivity)
                    builder.setMessage("This app needs Internet at the first run. Please turn on your data to get the latest currency rate from the internet.")
                        .setTitle("Lazy Cashier needs internet")
                        .setCancelable(false)
                        .setPositiveButton(
                            "Turn On Wifi or Data"
                        ) { dialog, id ->
                            val i = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                            startActivity(i)

                        }
                        .setNegativeButton(
                            "retry"
                        ) { dialog, id ->
                            val intent = intent
                            finish()
                            startActivity(intent)
                        }
                    val alert: android.app.AlertDialog? = builder.create()
                    alert!!.show()
                }
            })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
