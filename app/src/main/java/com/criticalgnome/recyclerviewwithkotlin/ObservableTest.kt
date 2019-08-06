package com.criticalgnome.recyclerviewwithkotlin

import android.app.Application
import android.icu.text.IDNA
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

data class InfoSutochno(val status: String, val data: SutochnoResponse? = null)

class ObservableTest: Application() {

    lateinit var consumerMan: MainActivity

    public var connection: Int? = 0

    override fun onCreate() {
        super.onCreate()
    }

    public fun setConsumer(consumer: MainActivity) {
        this.consumerMan = consumer
    }

    private fun sendCallBackToConsumer(response: InfoSutochno) {
        this.consumerMan.getCallBack(response)
    }

    public fun getConnectionStatus(): Int? {
        return this.connection
    }

    public fun sendRequest() {
        val okHttpClient = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(MainActivity.BaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val listCollector = mutableListOf<Collector>()

        val service = retrofit.create(SutochnoService::class.java)
        val call = service.getData()
        connection = 1
        call.enqueue(object : Callback<SutochnoResponse> {
            override fun onResponse(call: Call<SutochnoResponse>, response: Response<SutochnoResponse>) {
                response.body()
                connection = 0
                println("FIRSTSSSS34")
                var result = InfoSutochno("success", response.body())
                sendCallBackToConsumer(result)
               // observerTest.notifyObservers(result)
            }
            override fun onFailure(call: Call<SutochnoResponse>, t: Throwable) {
                connection = 0
                sendCallBackToConsumer(InfoSutochno("error"))
               // observerTest.notifyObservers()
            }
        })
    }
}