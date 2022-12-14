package com.donativ.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product (
        val user_id: String = "",
        val user_name: String = "",
        val title: String = "",
        val price: String = "",
        val description: String = "",
        val quantity: String = "",
        val category: String = "",
        val image: String = "",
        var product_id: String = "",
): Parcelable