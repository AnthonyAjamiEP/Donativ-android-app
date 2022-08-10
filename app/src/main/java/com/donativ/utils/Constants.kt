package com.donativ.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS: String = "users"
    const val PRODUCTS :String = "products"
    const val REQUESTED_PRODUCTS: String = "requested_products"

    const val DONATIV_PREFERENCES: String = "donativPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val MALE: String = "male"
    const val FEMALE: String = "female"
    const val FIRST_NAME: String = "firstName"
    const val LAST_NAME: String = "lastName"
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val IMAGE: String = "image"
    const val COMPLETE_PROFILE: String = "profileCompleted"

    const val PRODUCT_IMAGE:String = "Product_Image"
    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val USER_ID: String = "user_id"
    const val TITLE: String = "title"


    const val EXTRA_PRODUCT_ID: String = "extra_product_id"

    fun showImageChooser(activity: Activity)
    {
        // intent for launching image selection in phone gallery
        var galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        // launch the previous intent using the Constant code
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    // returns the extension of a given file (image in our case) - to be used in uploading to cloud storage
    fun getFileExtension(activity: Activity, uri: Uri?): String?
    {
        return MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}