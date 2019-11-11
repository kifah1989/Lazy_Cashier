package com.example.lazycashier

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            if (!validateHave() or !validateItem() or !validateCalculation()) {
                return@setOnClickListener
            }

            var i_have = HaveTxt.text.toString().toDouble()
            var item_price = ItemPriceTxt.text.toString().toDouble()

            var valueReturnDollar = 0.0
            var valueReturnLira = 0.0
            var havetolira = i_have * 1500
            var itemtolira = item_price * 1500

            if (HaveSwitch1.isChecked && ItemSwitch.isChecked) {

                valueReturnDollar = (i_have - item_price)

                valueReturnLira = (i_have - item_price) * 1500

                //LiraReturnTxtView.text = "you paid ${"%.1f".format(i_have)}${HaveSwitch1.text} and the item price is ${"%.1f".format(item_price)}${ItemSwitch.text} the shop should return ${"%.1f".format(valueReturnDollar)}\$ or ${"%,d".format(valueReturnLira.toInt())}L.L"
                returnInD.setText("${"%.1f".format(valueReturnDollar)}")
                returnInL.setText("${valueReturnLira.toInt()}")

            } else if (!HaveSwitch1.isChecked && !ItemSwitch.isChecked) {

                valueReturnDollar = ((i_have - item_price) / 1500)

                valueReturnLira = (i_have - item_price)

                //LiraReturnTxtView.text = "you paid ${"%,d".format(i_have.toInt())}${HaveSwitch1.text} and the item price is ${"%,d".format(item_price.toInt())}${ItemSwitch.text} the shop should return ${"%.1f".format(valueReturnDollar)}\$ or ${"%,d".format(valueReturnLira.toInt())}L.L"
                returnInD.setText("${"%.1f".format(valueReturnDollar)}")
                returnInL.setText("${valueReturnLira.toInt()}")

            } else if (HaveSwitch1.isChecked && !ItemSwitch.isChecked) {


                valueReturnDollar = ((havetolira - item_price) / 1500)

                valueReturnLira = (havetolira - item_price)

                // LiraReturnTxtView.text = "you paid ${"%.1f".format(i_have)}${HaveSwitch1.text} and the item price is ${"%,d".format(item_price.toInt())}${ItemSwitch.text} the shop should return ${"%.1f".format(valueReturnDollar)}\$ or ${"%,d".format(valueReturnLira.toInt())}L.L"
                returnInD.setText("${"%.1f".format(valueReturnDollar)}")
                returnInL.setText("${valueReturnLira.toInt()}")

            } else if (!HaveSwitch1.isChecked && ItemSwitch.isChecked) {

                valueReturnDollar = ((i_have - itemtolira) / 1500)

                valueReturnLira = (i_have - itemtolira)

                //LiraReturnTxtView.text = "you paid ${"%,d".format(i_have.toInt())}${HaveSwitch1.text} and the item price is ${"%.1f".format(item_price)}${ItemSwitch.text} the shop should return ${"%.1f".format(valueReturnDollar)}\$ or ${"%,d".format(valueReturnLira.toInt())}L.L"
                returnInD.setText("${"%.1f".format(valueReturnDollar)}")
                returnInL.setText("${valueReturnLira.toInt()}")
            }

        }

    }

    private fun validateHave(): Boolean {
        val ihave = HaveTxt.text.toString().trim()

        if (ihave.isEmpty()) {
            HaveTxt.error = "Field can't be empty"
            //LiraReturnTxtView.text = "Field can't be empty"

            return false
        } else if (HaveSwitch1.isChecked && ihave.length > 3 || !HaveSwitch1.isChecked && ihave.length > 6) {
            HaveTxt.error = "WoOo you have lots of money"
            //LiraReturnTxtView.text = "WoOo you have lots of money"

            return false
        } else {
            HaveTxt.error = null
            return true
        }
    }

    private fun validateItem(): Boolean {
        val item = ItemPriceTxt.text.toString().trim()

        return if (item.isEmpty()) {
            ItemPriceTxt.error = "Field can't be empty"
            //LiraReturnTxtView.text = "Field can't be empty"

            false
        } else if (ItemSwitch.isChecked && item.length > 3 || !ItemSwitch.isChecked && item.length > 6) {
            ItemPriceTxt.error = "you broke"
            // LiraReturnTxtView.text=  "you broke"

            false
        } else {
            ItemPriceTxt.error = null
            true
        }
    }

    private fun validateCalculation(): Boolean {
        val ihave = HaveTxt.text.toString().trim()
        val item_price = ItemPriceTxt.text.toString().trim()

        if (ihave.isEmpty()) {
            HaveTxt.error = "Field can't be empty"
            //LiraReturnTxtView.text = "WoOo you have lots of money"

            return false
        }
        if (item_price.isEmpty()) {
            ItemPriceTxt.error = "Field can't be empty"
            //LiraReturnTxtView.text = "Field can't be empty"

            return false
        } else if (!HaveSwitch1.isChecked && !ItemSwitch.isChecked && ihave.toDouble() < item_price.toDouble()) {
            HaveTxt.error = "you broke"
            //LiraReturnTxtView.text= "you broke"

            return false
        } else if (HaveSwitch1.isChecked && ItemSwitch.isChecked && ihave.toDouble() < item_price.toDouble()) {
            HaveTxt.error = "you broke"
            //LiraReturnTxtView.text = "you broke"

            return false
        } else if (HaveSwitch1.isChecked && !ItemSwitch.isChecked && ihave.toDouble() < item_price.toDouble() / 1500) {
            HaveTxt.error = "you broke"
            //LiraReturnTxtView.text= "you broke"

            return false
        } else if (!HaveSwitch1.isChecked && ItemSwitch.isChecked && ihave.toDouble() / 1500 < item_price.toDouble()) {
            HaveTxt.error = "you broke"
            //LiraReturnTxtView.text = "you broke"

            return false
        } else {
            HaveTxt.error = null
            ItemPriceTxt.error = null

            return true
        }
    }


}