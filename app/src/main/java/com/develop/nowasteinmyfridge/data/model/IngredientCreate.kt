package com.develop.nowasteinmyfridge.data.model

data class IngredientCreate(
    val name: String = "",
    val quantity: Int = 0,
    val image: Any,
    val mfg: String = "",
    val efd: String = "",
    val isInFreeze: Boolean = false,
    val isAddFromBarcode: Boolean = false,
)
