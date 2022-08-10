package com.donativ.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.donativ.R
import kotlinx.android.synthetic.main.activity_function.*


class FunctionActivity : View.OnClickListener, BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function)

        btn_donate.setOnClickListener(this)
        btn_search_for_products.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id) {
            R.id.action_settings -> {
                startActivity(Intent(this@FunctionActivity, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        if (view != null)
            when (view.id){
                R.id.btn_donate -> {
                    val intent = Intent(this@FunctionActivity, DonorMainActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_search_for_products -> {
                    val intent = Intent(this@FunctionActivity, CategoryActivity::class.java)
                    startActivity(intent)
                }
            }
    }
    override fun onBackPressed() {
        doubleBackToExit()
    }
}