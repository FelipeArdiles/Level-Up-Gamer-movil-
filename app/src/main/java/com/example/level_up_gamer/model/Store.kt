package com.example.level_up_gamer.model

import com.mapbox.geojson.Point

data class Store(
    val id: String,
    val name: String,
    val address: String,
    val phone: String,
    val location: Point
)
