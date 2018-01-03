package com.tumpraktikum.roboticsimulatorcontroller.main

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.tumpraktikum.roboticsimulatorcontroller.R

/**
 * Created by patriccorletto on 12/25/17.
 */
class BluetoothListAdapter(context: Context) : BaseAdapter(){

    private var mItems :ArrayList<BluetoothDevice> = ArrayList()

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ListRowHolder
        if (convertView == null) {
            view = this.mInflater.inflate(R.layout.list_row, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        vh.label.text = mItems[position].name
        return view
    }

    override fun getItem(position: Int): Any {
        return mItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mItems.size
    }

    private class ListRowHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.label) as TextView

    }
}