package com.donativ.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.donativ.R
import com.donativ.models.Product
import com.donativ.models.RequestedProduct
import com.donativ.ui.activities.RequestedProductDetailsActivity
import com.donativ.ui.activities.UserProductDetailsActivity
import com.donativ.ui.activities.ViewListedProductsActivity
import com.donativ.ui.activities.ViewRequestedProductsActivity
import com.donativ.utils.Constants
import com.donativ.utils.GliderLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

open class RequestedProductsAdapter (
        private val context: Context,
        private val list: ArrayList<RequestedProduct>,
        private val activity: ViewRequestedProductsActivity
):RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_list_layout,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder)
        {
            holder.itemView.tv_item_name.text = model.title
            holder.itemView.tv_item_price.text = ""

            holder.itemView.ib_delete_product.setOnClickListener{
                activity.deleteRequestedProduct(model.product_id)
            }

            holder.itemView.setOnClickListener{
                val intent = Intent(context, RequestedProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view:View): RecyclerView.ViewHolder(view)
}