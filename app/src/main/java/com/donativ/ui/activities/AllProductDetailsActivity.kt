package com.donativ.ui.activities

import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.donativ.R
import com.donativ.firestore.FirestoreClass
import com.donativ.models.Product
import com.donativ.models.User
import com.donativ.utils.Constants
import com.donativ.utils.GliderLoader
import kotlinx.android.synthetic.main.activity_all_product_details.*
import kotlinx.android.synthetic.main.activity_settings.*

class AllProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductId:String = ""
    private var mPhoneNumber:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_product_details)
        setupActionBar()
        tv_all_product_details_phone_number.setOnClickListener(this)

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID))
        {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        getProductDetails()
    }

    // setup an action bar on top of the screen that contains page name and a back button to go back to previous open activity
    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_all_product_details_activity)

        val actionBar = supportActionBar
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_all_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of the FirestoreClass to get the product details.
        FirestoreClass().getAllProductDetails(this@AllProductDetailsActivity, mProductId)
    }

    fun productDetailsSuccess(product: Product, user: User) {

        hideProgressdialog()

//      Populate the product details in the UI.

        GliderLoader(this@AllProductDetailsActivity).loadProductPicture(
                product.image,
                iv_all_product_detail_image
        )

        tv_all_product_details_phone_number.text = user.mobile.toString()
        mPhoneNumber = tv_all_product_details_phone_number.text.toString()
        tv_all_product_details_contact_name.text = "Contact Name: ${user.firstName} ${user.lastName}"
        tv_all_product_details_title.text = product.title
        tv_all_product_details_price.text =  "Price: ${product.price}"
        tv_all_product_details_description.text = product.description
        tv_all_product_details_available_quantity.text = product.quantity
    }

    // dial seller phone number when click on phone number
    override fun onClick(view: View?) {
        if(view != null){
            when(view.id ){
                R.id.tv_all_product_details_phone_number -> {
                    val intent = Intent(ACTION_DIAL)
                    intent.data = Uri.parse("tel:" + mPhoneNumber)
                    startActivity(intent)
                }
            }
        }
    }

}