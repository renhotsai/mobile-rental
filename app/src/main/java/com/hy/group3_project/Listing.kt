package com.hy.group3_project

import java.sql.Struct

data class  Listing(
    val img: String,
    val price: Int,
    val type: String,
    val rooms: Int,
    val bath: Int,
    val location: String,
    val isFavorite: Boolean
) {
    companion object {
        private var counter = 0
    }

    init {
        counter++
    }

    val id = counter

    // Secondary constructor with default values
    constructor() : this(
        img = "rental_1",
        price = 2000,
        type = "Apartment",
        rooms = 3,
        bath = 2,
        location = "123 ABC, Toronto",
        isFavorite = false
    )
}
