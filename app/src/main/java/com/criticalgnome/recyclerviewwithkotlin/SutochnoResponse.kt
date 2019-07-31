package com.criticalgnome.recyclerviewwithkotlin

import com.google.gson.annotations.SerializedName

class SutochnoResponse {

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("data")
    var data: Data? = null
}

class Data {
    @SerializedName("review")
    var review: Review? = null
}

class Review {
    @SerializedName("properties")
    var properties: ArrayList<Properties>? = null
}

class Properties {
    @SerializedName("group_name")
    var groupName: String = ""
    @SerializedName("grop_title")
    var gropTitle: String = ""
    @SerializedName("items")
    var items: ArrayList<Items>? = null
}

class Items {
    @SerializedName("key")
    var key: String = ""
    @SerializedName("title")
    var title: String = ""
    @SerializedName("value")
    var value: String = ""
}





/*
class Data {
    @SerializedName("review")
    var review: Review? = null
}

class Review {
    @SerializedName("properties")
    var properties: Properties? = null
}

class Properties {
    @SerializedName("items")
    val items: List<Items>? = null

    data class Items(var key: String, var title: String, var value: String)
}*/


