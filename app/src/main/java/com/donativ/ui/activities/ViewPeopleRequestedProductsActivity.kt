package com.donativ.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.donativ.R
import com.donativ.firestore.FirestoreClass
import com.donativ.models.Product
import com.donativ.models.RequestedProduct
import com.donativ.ui.adapters.AllProductsListAdapter
import com.donativ.ui.adapters.PeopleRequestedProductsAdapter
import kotlinx.android.synthetic.main.activity_all_products.*
import kotlinx.android.synthetic.main.activity_all_products.rv_dashboard_items
import kotlinx.android.synthetic.main.activity_all_products.tv_no_dashboard_items_found
import kotlinx.android.synthetic.main.activity_view_people_requested_products.*

class ViewPeopleRequestedProductsActivity : BaseActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_people_requested_products)
        getAllRequestedProductsListFromFireStore()
    }

    fun successPeopleRequestedProductsListFromFireStore (allProductsList: ArrayList<RequestedProduct>)
    {
        hideProgressdialog()

        if (allProductsList.size > 0)
        {
            rv_requested_dashboard_items.visibility = View.VISIBLE
            tv_no_requested_dashboard_items_found.visibility = View.GONE

            // how many items per row
            rv_requested_dashboard_items.layoutManager = GridLayoutManager(this,2)
            rv_requested_dashboard_items.setHasFixedSize(true)

            val adapterProducts = PeopleRequestedProductsAdapter(this,allProductsList)
            rv_requested_dashboard_items.adapter = adapterProducts
        }else
        {
            rv_requested_dashboard_items.visibility = View.GONE
            tv_no_requested_dashboard_items_found.visibility = View.VISIBLE
        }
    }

    private fun getAllRequestedProductsListFromFireStore()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductsList(this)
    }

}