package com.donativ.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.donativ.R
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException


// glide will make it easier to load images when adding products
class GliderLoader(val context: Context) {

    fun loadUserPicture(image: Any, imageView: ImageView)
    {
        try {
            Glide
                .with(context)
                .load(Uri.parse(image.toString())) // URI of the image
                .centerCrop()   // Scale type of image
                .placeholder(R.drawable.ic_user_placeholder) // default placeholder if image failed to load
                .into(imageView) // how the image is going to be loaded

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadProductPicture(image: Any, imageView: ImageView)
    {
        try {
            Glide
                    .with(context)
                    .load(Uri.parse(image.toString())) // URI of the image
                    .centerCrop()   // Scale type of image
                    .into(imageView) // how the image is going to be loaded

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}