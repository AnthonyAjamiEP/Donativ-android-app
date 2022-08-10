package com.donativ.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.donativ.R
import com.donativ.utils.Constants
import com.donativ.utils.GliderLoader
import com.google.firebase.auth.FirebaseAuth
import com.donativ.firestore.FirestoreClass
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.iv_user_photo
import kotlinx.android.synthetic.main.activity_user_profile.*

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: com.donativ.models.User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupActionBar()

        btn_logout.setOnClickListener(this)
        tv_edit.setOnClickListener(this)
        iv_user_photo.setOnClickListener(this)
    }

    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails()
    {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: com.donativ.models.User)
    {
        mUserDetails = user

        hideProgressdialog()

        GliderLoader(this@SettingsActivity).loadUserPicture(user.image,iv_user_photo)
        tv_name.text = "${user.firstName} ${user.lastName}"
        tv_gender.text = user.gender
        tv_email.text = user.email
        tv_mobile_number.text = "${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(view: View?) {
        if(view != null){
            when(view.id ){
                R.id.tv_edit -> {
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS,mUserDetails)
                    startActivity(intent)
                }
                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
//                R.id.iv_user_photo -> {
//                    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                            == PackageManager.PERMISSION_GRANTED
//                    ) {
//                    // showErrorSnackBar("You already have permission to storage.",false)
//                        Constants.showImageChooser(this)
//                        GliderLoader(this@SettingsActivity).loadUserPicture(mUserDetails.image, iv_user_photo)
//                    }
//                    // ask for permission if it is not already given
//                    else {
//                        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//                                Constants.READ_STORAGE_PERMISSION_CODE)
//                    }
//                }
            }
        }
    }
}