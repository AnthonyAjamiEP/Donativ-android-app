package com.donativ.ui.activities

import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.donativ.R
import com.donativ.firestore.FirestoreClass
import com.donativ.models.Product
import com.donativ.models.RequestedProduct
import com.donativ.models.User
import com.donativ.utils.Constants
import com.donativ.utils.GliderLoader
import kotlinx.android.synthetic.main.activity_all_product_details.*
import kotlinx.android.synthetic.main.activity_all_product_details.toolbar_all_product_details_activity
import kotlinx.android.synthetic.main.activity_all_product_details.tv_all_product_details_contact_name
import kotlinx.android.synthetic.main.activity_all_product_details.tv_all_product_details_description
import kotlinx.android.synthetic.main.activity_all_product_details.tv_all_product_details_title
import kotlinx.android.synthetic.main.activity_people_requested_product_details.*
import kotlinx.android.synthetic.main.activity_settings.*

class PeopleRequestedProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductId:String = ""
    private var mPhoneNumber:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people_requested_product_details)
        setupActionBar()
        tv_people_requested_product_details_phone_number.setOnClickListener(this)

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID))
        {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        getProductDetails()
    }

    // setup an action bar on top of the screen that contains page name and a back button to go back to previous open activity
    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_people_requested_product_details_activity)

        val actionBar = supportActionBar
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_people_requested_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of the FirestoreClass to get the product details.
        FirestoreClass().getPeopleRequestedProductDetails(this@PeopleRequestedProductDetailsActivity, mProductId)
    }

    fun RequestedProductDetailsSuccess(product: RequestedProduct, user: User) {
        hideProgressdialog()

//      Populate the requested product details in the UI.
        tv_people_requested_product_details_phone_number.text = user.mobile.toString()
        mPhoneNumber = tv_people_requested_product_details_phone_number.text.toString()
        tv_people_requested_product_details_contact_name.text = "Contact Name: ${user.firstName} ${user.lastName}"
        tv_people_requested_all_product_details_title.text = product.title
        tv_people_requested_all_product_details_description.text = product.description
    }

    // dial seller phone number when click on phone number
    override fun onClick(view: View?) {
        if(view != null){
            when(view.id ){
                R.id.tv_people_requested_product_details_phone_number -> {
                    val intent = Intent(ACTION_DIAL)
                    intent.data = Uri.parse("tel:" + mPhoneNumber)
                    startActivity(intent)
                }
            }
        }
    }

}