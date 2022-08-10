package com.donativ.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.donativ.R
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        btn_view_products.setOnClickListener(this)
        btn_request_product.setOnClickListener(this)
        btn_view_requested_products.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        if (view != null)
            when (view.id){
                R.id.btn_view_products -> {
                    val intent = Intent(this@CategoryActivity, AllProductsActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_request_product -> {
                    val intent = Intent(this@CategoryActivity, AddRequestedProductActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_view_requested_products -> {
                    val intent = Intent(this@CategoryActivity, ViewRequestedProductsActivity::class.java)
                    startActivity(intent)
                }
            }
    }
}