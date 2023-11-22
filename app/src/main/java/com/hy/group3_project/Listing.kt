package com.hy.group3_project

import java.sql.Struct

data class  Listing(
    val img: String,
    val price: String,
    val type: String,
    val rooms: String,
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
        img = "1",
        price = "$ 2000",
        type = "Apartment",
        rooms = "3 Rooms | 2 Bath",
        location = "123 ABC, Toronto",
        isFavorite = false
    )
}
