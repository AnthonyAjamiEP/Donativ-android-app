package com.donativ.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.donativ.R
import com.donativ.firestore.FirestoreClass
import com.donativ.models.Product
import com.donativ.models.RequestedProduct
import com.donativ.utils.Constants
import com.donativ.utils.GliderLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_add_product.toolbar_add_product_activity
import kotlinx.android.synthetic.main.activity_add_requested_product.*
import java.io.IOException

//Add Product screen of the app.
class AddRequestedProductActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_requested_product)

        setupActionBar()

        btn_submit_add_requested_product.setOnClickListener(this)
        btn_submit_add_requested_product.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {
                R.id.btn_submit_add_requested_product -> {
                    if (validateRequestedProductDetails())
                    {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        uploadRequestedProductDetails()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@AddRequestedProductActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_requested_product_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_add_requested_product_activity.setNavigationOnClickListener { onBackPressed() }
    }

//     * A function to validate the product details.

    private fun validateRequestedProductDetails(): Boolean {
        return when {

            TextUtils.isEmpty(et_requested_product_title.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_product_title),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_requested_product_description.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_product_description),
                    true
                )
                false
            }

            else -> {
                true
            }
        }
    }

    private fun uploadRequestedProductDetails() {

        // Get the logged in username from the SharedPreferences that we have stored at the time of login.
        val username =
            this.getSharedPreferences(Constants.DONATIV_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME, "")!!

        // Here we get the text from editText and trim the space
        val requestedProduct = RequestedProduct(
            FirestoreClass().getCurrentUserID(),
            username,
            et_requested_product_title.text.toString().trim { it <= ' ' },
            et_requested_product_description.text.toString().trim { it <= ' ' },
        )

        FirestoreClass().uploadRequestedProductDetails(this@AddRequestedProductActivity, requestedProduct)
    }

    fun requestedProductUploadSuccess() {
        hideProgressdialog()

        Toast.makeText(
            this@AddRequestedProductActivity,
            resources.getString(R.string.product_uploaded_success_message),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}