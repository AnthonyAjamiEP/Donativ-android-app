package com.donativ.ui.activities

import android.os.Bundle
import com.donativ.R
import com.donativ.firestore.FirestoreClass
import com.donativ.models.Product
import com.donativ.utils.Constants
import com.donativ.utils.GliderLoader
import kotlinx.android.synthetic.main.activity_user_product_details.*
import kotlinx.android.synthetic.main.activity_settings.*

class UserProductDetailsActivity : BaseActivity() {

    private var mProductId:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_product_details)
        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID))
        {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        getProductDetails()
    }

    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_product_details_activity)

        val actionBar = supportActionBar
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of the FirestoreClass to get the product details.
        FirestoreClass().getUserProductDetails(this@UserProductDetailsActivity, mProductId)
    }

    fun productDetailsSuccess(product: Product) {

        hideProgressdialog()

        // Populate the product details in the UI.
        GliderLoader(this@UserProductDetailsActivity).loadProductPicture(
            product.image,
            iv_product_detail_image
        )

        tv_product_details_title.text = product.title
        tv_product_details_price.text =  "Price: ${product.price}"
        tv_product_details_description.text = product.description
        tv_product_details_available_quantity.text = product.quantity
    }

//    fun getPhoneNumberSuccess(user: User)
//    {
//        // Populate the product seller phone number in the UI.
//        tv_product_details_phone_number.text = user.mobile.toString()
//
//    }
}