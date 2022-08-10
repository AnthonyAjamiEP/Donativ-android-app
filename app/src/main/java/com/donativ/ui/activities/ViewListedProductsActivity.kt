package com.donativ.ui.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.donativ.R
import com.donativ.firestore.FirestoreClass
import com.donativ.models.Product
import com.donativ.ui.adapters.MyProductsListAdapter
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_view_listed_products.*

class ViewListedProductsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_listed_products)
        getProductsListFromFireStore()
    }

    fun successProductsListFromFireStore (productsList: ArrayList<Product>)
    {
        hideProgressdialog()

        if (productsList.size > 0)
        {
            rv_my_product_items.visibility = View.VISIBLE
            tv_no_products_found.visibility = View.GONE

            rv_my_product_items.layoutManager = LinearLayoutManager(this)
            rv_my_product_items.setHasFixedSize(true)
            val adapterProducts = MyProductsListAdapter(this,productsList,this)

            rv_my_product_items.adapter = adapterProducts
        }else
        {
            rv_my_product_items.visibility = View.GONE
            tv_no_products_found.visibility = View.VISIBLE
        }
    }

    private fun getProductsListFromFireStore()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductsList(this)
    }

    fun deleteProduct(productID: String)
    {
        showAlertDialogToDeleteProduct(productID)
    }
    fun productDeleteSuccess() {
        hideProgressdialog()

        Toast.makeText(
            this,
            resources.getString(R.string.product_delete_success_message),
            Toast.LENGTH_SHORT
        ).show()

        // Get the latest products list from cloud firestore.
        getProductsListFromFireStore()
    }
    private fun showAlertDialogToDeleteProduct(productID: String) {

        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            showProgressDialog(resources.getString(R.string.please_wait))

            // Call the function of FireStore class.
            FirestoreClass().deleteProduct(this@ViewListedProductsActivity, productID)

            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}