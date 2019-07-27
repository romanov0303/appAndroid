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
                MainItem("Соответсвие фото", "Пушкин"),
                MainItem("Недостатки", "Лермонтов"),
                MainItem("Расположение", "Лермонтов"),
                MainItem("Чистота", "Лермонтов"),
                MainItem("Обслуживание", "Лермонтов"),
                MainItem("Удобство номера", "Лермонтов"),
                MainItem("Цена - качество или в две строчки", "Лермонтов")
        )

        val myAdapter = MainAdapter(items)

        myRecycler.adapter = myAdapter
    }

}
