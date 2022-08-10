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
import com.donativ.ui.adapters.AllProductsListAdapter
import kotlinx.android.synthetic.main.activity_all_products.*

class AllProductsActivity : BaseActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_products)
        getAllProductsListFromFireStore()
    }

    fun successAllProductsListFromFireStore (allProductsList: ArrayList<Product>)
    {
        hideProgressdialog()

        if (allProductsList.size > 0)
        {
            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.GONE

            // how many items per row
            rv_dashboard_items.layoutManager = GridLayoutManager(this,2)
            rv_dashboard_items.setHasFixedSize(true)

            val adapterProducts = AllProductsListAdapter(this,allProductsList)
            rv_dashboard_items.adapter = adapterProducts
        }else
        {
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_items_found.visibility = View.VISIBLE
        }
    }

    fun successFilteredProductsListFromFireStore(filteredProductsList: ArrayList<Product>)
    {
        hideProgressdialog()

        if (filteredProductsList.size > 0)
        {
            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.GONE

            // how many items per row
            rv_dashboard_items.layoutManager = GridLayoutManager(this,2)
            rv_dashboard_items.setHasFixedSize(true)

            val adapterProducts = AllProductsListAdapter(this,filteredProductsList)
            rv_dashboard_items.adapter = adapterProducts
        }else
        {
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_items_found.visibility = View.VISIBLE
        }
    }

    private fun getAllProductsListFromFireStore()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductsList(this)
    }

    private fun getFilteredProductsListFromFireStore(filteredText: String)
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getFilteredProductsList(this, filteredText)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_search_view_menu,menu)

        var menuItem = menu!!.findItem(R.id.search_view_product)
        var searchView = menuItem.actionView as SearchView

        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    getFilteredProductsListFromFireStore(query)
                    hideProgressdialog()
                }
                else {
                    getAllProductsListFromFireStore()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })

        return true
    }
}