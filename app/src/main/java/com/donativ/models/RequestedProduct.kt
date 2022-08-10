package com.donativ.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestedProduct (
    val user_id: String = "",
    val user_name: String = "",
    val title: String = "",
    val description: String = "",
    var product_id: String = "",
): Parcelable