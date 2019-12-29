package com.example.lazycashier

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lazycashier.R.drawable.*
import com.google.android.material.textfield.TextInputLayout
import de.tobiasschuerg.money.Currency
import de.tobiasschuerg.money.Money
import kotlinx.android.synthetic.main.main_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity() {
    private var rates: SortedMap<String, Double>? = null
    private var timestamp: Long? = null
    private var currencies: Currencies? = null
    private var currencyList = Array<Currency?>(11) { null }
    private var valueList: DoubleArray = DoubleArray(11)
    lateinit var iHaveCurrency: Currency
    lateinit var itemCurrency: Currency
    private var mSpinner: Spinner? = null
    private var mSpinner2: Spinner? = null
    private var codeList = Array<String?>(11) { null }
    var moneyList = Array<Money?>(11) { null }
    private val flags = arrayOf(
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
    private val currencyName = arrayOf(
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


    private fun initializeui() {
        val mCustomAdapter = SpinnerAdapter(this@MainActivity, currencyName, flags)
        spinner.adapter = mCustomAdapter
        spinner2.adapter = mCustomAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        getJason()
        initializeui()

        //USD,AED,EUR,LBP,AUD.BHD,EGP,GBP,QAR,SAR,SYP,INR

    }

    override fun onSaveInstanceState(outState: Bundle) {


        val first = spinner.selectedItemPosition
        val second = spinner2.selectedItemPosition
        outState.putInt("first", first)
        outState.putInt("first", second)
        outState.putLong("timestamp", timestamp!!)
        outState.putDoubleArray("valueList", valueList)
        outState.putStringArray("codeList", codeList)


        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val pos = savedInstanceState.getInt("first")
        val pos2 = savedInstanceState.getInt("second")
        spinner.setSelection(pos)
        spinner2.setSelection(pos2)

        val time = savedInstanceState.getLong("timestamp")
        timestamp = time
        val vlList = savedInstanceState.getDoubleArray("valueList")
        valueList = vlList!!
        val codList = savedInstanceState.getStringArray("codeList")
        codeList = codList!!
        super.onRestoreInstanceState(savedInstanceState)
    }


    fun click(view: View) {

        calculate()
    }

    fun calculate() {

        val editText = findViewById<EditText>(R.id.editText)
        val editText2 = findViewById<EditText>(R.id.editText2)
        val textInputLayout = findViewById<TextInputLayout>(R.id.textInputLayout)
        val textInputLayout2 = findViewById<TextInputLayout>(R.id.textInputLayout2)



        val currencyListview = findViewById<RecyclerView>(R.id.recyclerView)
        try {
            val ihave = editText.text.toString().toDouble()
            val itemPrice = editText2.text.toString().toDouble()
            val selectedCurrency1: Int = spinner.selectedItemPosition
            val selectedCurrency2: Int = spinner2.selectedItemPosition

            val iHaveMoney = Money(ihave, currencyList[selectedCurrency1]!!)
            val itemMoney = Money(itemPrice, currencyList[selectedCurrency2]!!)
            val ihavetoitem = iHaveMoney.convertInto(currencyList[selectedCurrency2]!!)
            val itemtoihave = itemMoney.convertInto(currencyList[selectedCurrency1]!!)
            val returnMoney1 = iHaveMoney - itemtoihave
            val returnMoney2 = ihavetoitem - itemMoney
            for (i in 0..10) {
                moneyList[i] = (returnMoney1.convertInto(currencyList[i]!!))
            }
            textView5.text = "= " + ihavetoitem
            textView6.text = "= " + itemtoihave
            when {
                iHaveMoney.amount.toDouble() < itemtoihave.amount.toDouble() -> {
                    textInputLayout.error = "not enough money"
                    textView5.text = ""
                    textView6.text = ""
                    textInputLayout2.isErrorEnabled = false

                }
                returnMoney1.amount.toInt() == returnMoney2.amount.toInt() -> {
                    textView5.text = "no change"
                }
                else -> {
                    currencyListview.layoutManager = LinearLayoutManager(this@MainActivity)
                    currencyListview.adapter = CurrencyAdapter(moneyList, flags, currencyName)
                    textInputLayout.isErrorEnabled = false
                    textInputLayout2.isErrorEnabled = false

                }
            }
        } catch (ex: Exception) {
            if (editText.text.isEmpty()) {
                textInputLayout.error = "please enter amount"
            } else
                textInputLayout.isErrorEnabled = false

            if (editText2.text.isEmpty()) {
                textInputLayout2.error = "please enter amount"
            } else
                textInputLayout2.isErrorEnabled = false

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

                    currencies = response.body()

                    timestamp = currencies!!.timestamp

                    val cal = Calendar.getInstance(Locale.ENGLISH)
                    cal.timeInMillis = timestamp!! * 1000
                    val date: String = DateFormat.format("dd-MM-yyyy @ hh:mm a", cal).toString()

                    textView2.text = "updated on " + date
                    rates = currencies!!.rates.toSortedMap()

                    var i = 0
                    for (values in rates!!.values) {
                        valueList[i] = values
                        i++
                    }
                    i = 0
                    for (keys in rates!!.keys) {
                        codeList[i] = keys
                        i++

                    }

                    for (i in 0..10) {
                        currencyList[i] = (Currency(codeList[i]!!, currencyName[i], valueList[i]))
                    }

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
