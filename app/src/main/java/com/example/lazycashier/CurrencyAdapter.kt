package com.example.lazycashier

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.tobiasschuerg.money.Money


class CurrencyAdapter(
    private val symbol: Array<Money?>,
    private val flags: Array<Int>,
    private val name: Array<String>
) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        i: Int
    ): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.currency_text_recycler, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: CurrencyViewHolder,
        i: Int
    ) {
        val title = symbol[i]
        viewHolder.txt.text = title.toString()
        viewHolder.m.setImageResource(flags[i])
        viewHolder.name.text = name[i]
    }

    override fun getItemCount(): Int {
        return symbol.size
    }


    inner class CurrencyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var m: ImageView = itemView.findViewById(R.id.curimg)
        var txt: TextView = itemView.findViewById(R.id.curtxt)
        var name: TextView = itemView.findViewById(R.id.curname)

    }

}