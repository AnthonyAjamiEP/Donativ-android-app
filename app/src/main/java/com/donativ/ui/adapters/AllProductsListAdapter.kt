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
import com.donativ.ui.activities.AllProductDetailsActivity
import com.donativ.ui.activities.AllProductsActivity
import com.donativ.utils.Constants
import com.donativ.utils.GliderLoader
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*

open class AllProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

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

            GliderLoader(context).loadProductPicture(
                model.image,
                holder.itemView.iv_dashboard_item_image
            )
            holder.itemView.tv_dashboard_item_title.text = model.title
            holder.itemView.tv_dashboard_item_price.text = model.price

            holder.itemView.setOnClickListener{
                val intent = Intent(context, AllProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
                context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getFilter(): Filter {

        return object: Filter() {
            override fun performFiltering(charsequence: CharSequence?): FilterResults? {

                var filterResults = FilterResults()
                if (charsequence == null || charsequence.isEmpty()) {
                    filterResults.count = listFilter.count()
                    filterResults.values = listFilter
                }else {
                    var searchChar: String = charsequence.toString().toLowerCase()
                    var productModal = ArrayList<Product>()
                    for (product in listFilter)
                    {
                        if (product.title.toLowerCase().contains(searchChar) || product.description.toLowerCase().contains(searchChar))
                        {
                            productModal.add(product)
                        }
                    }
                    filterResults.count = productModal.size
                    filterResults.values = productModal
                }
                return filterResults
            }


            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                list = p1!!.values as ArrayList<Product>
            }
        }
    }
}