package com.example.lazycashier

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.tobiasschuerg.money.Money


class CurrencyAdapter(
    private val data: ArrayList<Money>,
    private val flags: ArrayList<Int>
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
        val title = data[i]
        viewHolder.txt.text = title.toString()
        viewHolder.m.setImageResource(flags[i])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class CurrencyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var m: ImageView
        var txt: TextView

        init {
            m = itemView.findViewById(R.id.curimg)
            txt = itemView.findViewById(R.id.curtxt)
        }
    }

}