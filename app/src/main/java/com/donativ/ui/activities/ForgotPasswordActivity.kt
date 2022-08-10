package com.donativ.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.donativ.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setupActionBar()

        btn_submit.setOnClickListener{
            val email: String = et_email_forgot_password.text.toString().trim { it <= ' ' }

            if (email.isEmpty())
            {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
            }
            else
            {
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener {task ->
                            hideProgressdialog()
                            if (task.isSuccessful)
                            {
                                showErrorSnackBar(
                                        "Email sent. Please check your email", false
                                )
                                finish()
                            } else {
                                // If action is not successful => show error message.
                                showErrorSnackBar(task.exception!!.message.toString(), true)
                            }
                        }
            }

        }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_forgot_password_activity)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        toolbar_forgot_password_activity.setNavigationOnClickListener { onBackPressed() }
    }
}