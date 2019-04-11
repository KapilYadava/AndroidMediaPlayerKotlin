package com.example.androidmediaplayerkotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.list_item.view.*


class MyAdapter(private val context: MainActivity, private val list: MutableList<Song>): BaseAdapter() {

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, p2, false)
        view.textView.text = list[p0].title
        return view
    }

    override fun getItem(p0: Int): Any {
       return list[p0]
    }

    override fun getItemId(p0: Int): Long {
       return list[p0].id
    }

    override fun getCount(): Int {
       return list.size
    }

}