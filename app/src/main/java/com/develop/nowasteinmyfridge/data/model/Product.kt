package com.develop.nowasteinmyfridge.data.model

data class Product(
    val product: ProductDetails = ProductDetails(),
    val status: Int = 0,
)