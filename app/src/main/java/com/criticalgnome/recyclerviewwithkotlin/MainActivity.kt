package com.criticalgnome.recyclerviewwithkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val items = listOf(
                MainItem("Достоинства", "Пушкин"),
                MainItem("Недостатки", "Лермонтов"),
                MainItem("Недостатки", "Лермонтов"),
                MainItem("Недостатки", "Лермонтов"),
                MainItem("Недостатки", "Лермонтов"),
                MainItem("Недостатки", "Лермонтов"),
                MainItem("Недостатки", "Лермонтов"),
                MainItem("Недостатки", "Лермонтов"),
                MainItem("Недостатки", "Лермонтов"),
                MainItem("Недостатки", "Лермонтов")
        )

        val myAdapter = MainAdapter(items)

        myRecycler.adapter = myAdapter
        myRecycler.isNestedScrollingEnabled()
    }

}
