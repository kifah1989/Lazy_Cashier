package com.example.lazycashier

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by abdalla on 10/29/17.
 */
class SpinnerAdapter(
    var mContext: Context,
    var spinnerTitles: Array<String>,
    var spinnerImages: IntArray
) :
    ArrayAdapter<String?>(mContext, R.layout.custom_spinner) {
    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        return getView(position, convertView, parent)
    }

    override fun getCount(): Int {
        return spinnerTitles.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var mViewHolder = ViewHolder()
        if (convertView == null) {
            val mInflater =
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = mInflater.inflate(R.layout.custom_spinner, parent, false)
            mViewHolder.mFlag = convertView.findViewById<View>(R.id.img) as ImageView
            mViewHolder.mName = convertView.findViewById<View>(R.id.txt) as TextView
            convertView.tag = mViewHolder
        } else {
            mViewHolder = convertView.tag as ViewHolder
        }
        mViewHolder.mFlag!!.setImageResource(spinnerImages[position])
        mViewHolder.mName!!.text = spinnerTitles[position]
        return convertView!!
    }

    private class ViewHolder {
        var mFlag: ImageView? = null
        var mName: TextView? = null
    }

}