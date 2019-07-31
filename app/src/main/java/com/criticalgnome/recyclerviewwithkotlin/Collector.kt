package com.criticalgnome.recyclerviewwithkotlin

data class Collector(
        val groupName: String,
        val items: MutableList<Ratings>
)

data class Ratings(
    var key: String,
    var value: String
)