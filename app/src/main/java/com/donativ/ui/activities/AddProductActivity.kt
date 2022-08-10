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
import com.donativ.utils.Constants
import com.donativ.utils.GliderLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.IOException

//Add Product screen of the app.
class AddProductActivity : BaseActivity(), View.OnClickListener {

    // A global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null

    // A global variable for uploaded product image URL.
    private var  mProductImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        setupActionBar()

        iv_add_update_product.setOnClickListener(this)
        btn_submit_add_product.setOnClickListener(this)

        val mySpinner = findViewById<View>(R.id.product_category_spinner) as Spinner

        val myAdapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, resources.getStringArray(R.array.product_category_names))
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner.adapter = myAdapter;

    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                // The permission code is similar to the user profile image selection.
                R.id.iv_add_update_product -> {
                    if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                            == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this@AddProductActivity)
                    } else {
                        /*Requests permissions to be granted to the app. These permissions
                         must be requested in the manifest, they should not be granted to the app.*/
                        ActivityCompat.requestPermissions(
                                this,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.btn_submit_add_product -> {
                    if (validateProductDetails()) {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        if (mSelectedImageFileUri != null) {
                            uploadProductImage()
                        } else {
                            uploadProductDetails()
                        }
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
                Constants.showImageChooser(this@AddProductActivity)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {

            // Replace the add icon with edit icon once the image is selected.
            iv_add_update_product.setImageDrawable(
                    ContextCompat.getDrawable(
                            this@AddProductActivity,
                            R.drawable.ic_vector_edit
                    )
            )

            // The uri of selection image from phone storage.
            mSelectedImageFileUri = data.data!!

            try {
                // Load the product image in the ImageView.
                GliderLoader(this@AddProductActivity).loadUserPicture(
                        mSelectedImageFileUri!!,
                        iv_product_image
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_product_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_add_product_activity.setNavigationOnClickListener { onBackPressed() }
    }

//     * A function to validate the product details.

    private fun validateProductDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image), true)
                false
            }

            TextUtils.isEmpty(et_product_title.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title), true)
                false
            }

            TextUtils.isEmpty(et_product_price.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price), true)
                false
            }

            TextUtils.isEmpty(et_product_description.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_enter_product_description),
                        true
                )
                false
            }

            TextUtils.isEmpty(et_product_quantity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_enter_product_quantity),
                        true
                )
                false
            }
            else -> {
                true
            }
        }
    }

// A function to upload the selected product image to firebase cloud storage.

    private fun uploadProductImage()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadImageToCloudStorage(this@AddProductActivity, mSelectedImageFileUri, Constants.PRODUCT_IMAGE)
    }

//A function to get the successful result of product image upload.

    fun imageUploadSuccess(imageURL: String)
    {
        mProductImageURL = imageURL
        uploadProductDetails()
    }
//
    private fun uploadProductDetails() {

        // Get the logged in username from the SharedPreferences that we have stored at the time of login.
        val username =
            this.getSharedPreferences(Constants.DONATIV_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME, "")!!

        // Here we get the text from editText and trim the space
        val product = Product(
                FirestoreClass().getCurrentUserID(),
                username,
                et_product_title.text.toString().trim { it <= ' ' },
                et_product_price.text.toString().trim { it <= ' ' },
                et_product_description.text.toString().trim { it <= ' ' },
                et_product_quantity.text.toString().trim { it <= ' ' },
                product_category_spinner.selectedItem.toString(),
                mProductImageURL
        )

        FirestoreClass().uploadProductDetails(this@AddProductActivity, product)
    }

    fun productUploadSuccess() {
        hideProgressdialog()

        Toast.makeText(
                this@AddProductActivity,
                resources.getString(R.string.product_uploaded_success_message),
                Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}