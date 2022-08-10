package com.donativ.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.donativ.R
import com.donativ.models.User
import com.donativ.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.donativ.firestore.FirestoreClass
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_login_email
import kotlinx.android.synthetic.main.activity_login.et_login_password
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        btn_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)
        tv_forgot_password.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if(view != null){
            when(view.id ){
                R.id.tv_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_login -> {
                    loginRegisteredUser()
                }
                R.id.tv_register -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean{
        return when {
            TextUtils.isEmpty(et_login_email.text.toString().trim() {it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }
            TextUtils.isEmpty(et_login_password.text.toString().trim() {it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun loginRegisteredUser()
    {

        // Check with validateRegisterDetails() function if the output is true or false.
        if (validateLoginDetails())
        {

            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = et_login_email.text.toString().trim { it <= ' ' }
            val password: String = et_login_password.text.toString().trim { it <= ' ' }

            // Login using Firebase.
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {task ->

                                // If the login is successfully done
                                if (task.isSuccessful) {
                                    FirestoreClass().getUserDetails(this)
                                } else {
                                    hideProgressdialog()
                                    // If the login is not successful => show error message.
                                    showErrorSnackBar(task.exception!!.message.toString(), true)
                                }
                            }
        }
        }

    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideProgressdialog()

        if (user.profileCompleted == 0)
        {
            // Redirect the user to Profile Completion screen after log in if user hasn't completed his profile.
            val intent=Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }
        else
        {
            startActivity(Intent(this@LoginActivity, FunctionActivity::class.java))
        }
        finish()
    }
}