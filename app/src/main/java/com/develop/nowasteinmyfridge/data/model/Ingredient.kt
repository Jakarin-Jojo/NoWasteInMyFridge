package com.develop.nowasteinmyfridge.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Ingredient(
    val id: String = "",
    val name: String = "",
    var quantity: Int = 0,
    val image:String ="",
    val mfg:String ="",
    val efd:String ="",
    val isInFreezer: Boolean = false,
    val isAddFromBarcode: Boolean = false,
){
    fun isExpired(): Boolean {
        val dateFormat = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
        dateFormat.isLenient = false // Ensure strict date parsing

        return try {
            val expiryDate = dateFormat.parse(efd)
            val currentDate = Date()
            currentDate.after(expiryDate)
        } catch (e: Exception) {
            // Handle parsing exceptions or invalid date format
            e.printStackTrace()
            false // Treat as not expired if unable to parse
        }
    }
}