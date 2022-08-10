package com.donativ.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.donativ.R
import kotlinx.android.synthetic.main.activity_donor_main.*

class DonorMainActivity : AppCompatActivity() ,View.OnClickListener
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_main)

        btn_add_product.setOnClickListener(this)
        btn_view_added_products.setOnClickListener(this)
        btn_view_requested_products.setOnClickListener(this)
    }
    override fun onClick(view: View?) {
        if (view != null)
            when (view.id){
                R.id.btn_add_product -> {
                    val intent = Intent(this@DonorMainActivity, AddProductActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_view_added_products -> {
                    val intent = Intent(this@DonorMainActivity, ViewListedProductsActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_view_requested_products -> {
                    val intent = Intent(this@DonorMainActivity, ViewPeopleRequestedProductsActivity::class.java)
                    startActivity(intent)
                }
            }
    }
}