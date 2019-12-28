package com.example.androidcardlist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.card_list_item.view.*

class RecyclerAdapter(
    private val context: Context,
    private val itemClickListener: RecyclerAdapter.RecyclerViewHolder.ItemClickListener,
    private val itemList: List<String>):
    RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.card_list_item, parent, false))
    }
    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, position: Int) {

        // Kotlin スコープ関数 用途まとめ https://qiita.com/ngsw_taro/items/d29e3080d9fc8a38691e
        viewHolder?.let {
            it.itemTextView.text = itemList.get(position)
            it.itemImageView.setImageResource(R.mipmap.ic_launcher)
            // 参考 : Kotlinでリスナーやコールバックをスッキリと書く【関数リテラルとSAM変換】https://qiita.com/RyotaMurohoshi/items/01b370f34a4bf96f5c39
            it.itemView.setOnClickListener {
                itemClickListener.onItemClick(it, position)
            }

//            LogUtil.d("onBindViewHolder position : ", position.toString())
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class RecyclerViewHolder(view: View): RecyclerView.ViewHolder(view) {

        interface ItemClickListener {
            fun onItemClick(view: View, position: Int)
        }

        val itemTextView: TextView = view.itemTextView
        val itemImageView: ImageView = view.itemImageView

    }
}