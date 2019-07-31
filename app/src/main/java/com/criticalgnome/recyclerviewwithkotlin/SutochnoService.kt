package com.criticalgnome.recyclerviewwithkotlin

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SutochnoService {
    @GET("?t=1")
    fun getData(): Call<SutochnoResponse>
}

// https://api.myjson.com/bins/grhsp