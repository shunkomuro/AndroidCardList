package com.example.androidcardlist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

// TODO
// androidx 変換
// unit test

// 参考
// KotlinでRecyclerViewを使ったリスト表示を行う https://qiita.com/Todate/items/297bc3e4d0f3d2477ed3
// 【Android】RecyclerViewの基本的な実装 https://qiita.com/HideMatsu/items/a9ab48608e4f681d31fe
class MainActivity : AppCompatActivity(), RecyclerAdapter.RecyclerViewHolder.ItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed(
                { swipeRefreshLayout.isRefreshing = false }
                , 3000)
        }

        // val stringArray = arrayOf("A", "B", "C").toMutableList()
        val stringArray = arrayListOf<String>().toMutableList()
        for (i in 0..1000) {
            stringArray.add(i.toString())
        }

        recyclerView.adapter = RecyclerAdapter(this, this, stringArray)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(applicationContext, "position $position was tapped", Toast.LENGTH_SHORT).show()
    }
}
