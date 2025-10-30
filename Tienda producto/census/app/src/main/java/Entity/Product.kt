package com.example.census.Entity

import android.graphics.Bitmap

class Product(
    private var id: String,
    private var name: String,
    private var description: String,
    private var price: Double,
    private var image: Bitmap?
) {
    var ID: String
        get() = id
        set(value) { id = value }

    var Name: String
        get() = name
        set(value) { name = value }

    var Description: String
        get() = description
        set(value) { description = value }

    var Price: Double
        get() = price
        set(value) { price = value }

    var Image: Bitmap?
        get() = image
        set(value) { image = value }
}
