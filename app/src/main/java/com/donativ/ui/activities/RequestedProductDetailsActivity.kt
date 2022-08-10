package com.donativ.ui.activities

import android.os.Bundle
import com.donativ.R
import com.donativ.firestore.FirestoreClass
import com.donativ.models.Product
import com.donativ.models.RequestedProduct
import com.donativ.utils.Constants
import com.donativ.utils.GliderLoader
import kotlinx.android.synthetic.main.activity_requested_product_details.*
import kotlinx.android.synthetic.main.activity_user_product_details.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_user_product_details.tv_product_details_description

class RequestedProductDetailsActivity : BaseActivity() {

    private var mProductId:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requested_product_details)
        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID))
        {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        getRequestedProductDetails()
    }

    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_requested_product_details_activity)

        val actionBar = supportActionBar
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_requested_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getRequestedProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of the FirestoreClass to get the product details.
        FirestoreClass().getRequestedProductDetails(this@RequestedProductDetailsActivity, mProductId)
    }

    fun requestedProductDetailsSuccess(requestedProduct: RequestedProduct) {

        hideProgressdialog()

        tv_requested_product_details_title.text = requestedProduct.title
        tv_requested_product_details_description.text = requestedProduct.description
    }

//    fun getPhoneNumberSuccess(user: User)
//    {
//        // Populate the product seller phone number in the UI.
//        tv_product_details_phone_number.text = user.mobile.toString()
//
//    }
}