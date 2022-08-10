package com.donativ.ui.activities

import android.os.Bundle
import com.donativ.R

class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

//        supportActionBar!!.setBackgroundDrawable(
//                ContextCompat.getDrawable(
//                        this@DashboardActivity,
//                        R.drawable.bottom_nav_background
//                )
//        )

//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_donor, R.id.navigation_beneficiary,
////                R.id.navigation_profile
//        ))
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}