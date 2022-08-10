package com.donativ.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.donativ.R
import com.donativ.models.Product
import com.donativ.models.RequestedProduct
import com.donativ.ui.activities.AllProductDetailsActivity
import com.donativ.ui.activities.AllProductsActivity
import com.donativ.ui.activities.PeopleRequestedProductDetailsActivity
import com.donativ.ui.activities.ViewPeopleRequestedProductsActivity
import com.donativ.utils.Constants
import com.donativ.utils.GliderLoader
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*

open class PeopleRequestedProductsAdapter(
    private val context: Context,
    private var list: ArrayList<RequestedProduct>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listFilter = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            holder.itemView.tv_dashboard_item_title.text = model.title

            holder.itemView.setOnClickListener{
                val intent = Intent(context, PeopleRequestedProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
                context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}