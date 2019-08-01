package com.criticalgnome.recyclerviewwithkotlin

data class Collector(
        val groupName: String,
        val items: MutableMap<String, String>
)

data class Ratings(
    var key: String,
    var value: String
)