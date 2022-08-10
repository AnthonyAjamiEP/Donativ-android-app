package com.donativ.ui.activities

import android.app.Dialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.donativ.R
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private lateinit var mprogressDialog: Dialog

    // function to be used in other parts of code when we want to show bottom error pop-up ( either success or error)
    fun showErrorSnackBar(message:String,errorMessage: Boolean)
    {
        val snackBar = Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if(errorMessage) {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,R.color.colorSnackBarError))
        }else {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,R.color.colorSnackBarSuccess))
        }
        snackBar.show()
    }

    // progress dialog to be used all over the app
    fun showProgressDialog(text: String)
    {
        //get the object created in the top of this BaseActivity.kt
        mprogressDialog = Dialog(this)

        // set the dialog to the one already created in the layout folder
        mprogressDialog.setContentView(R.layout.dialog_progress)

        // dynamically change the progress dialog text to the text passed to this function
        mprogressDialog.tv_progress_text.text=text

        // cannot cancel it on any form of touch
        mprogressDialog.setCancelable(false)
        mprogressDialog.setCanceledOnTouchOutside(false)

        // start and display the dialog
        mprogressDialog.show()
    }
    fun hideProgressdialog()
    {
        mprogressDialog.dismiss()
    }

    fun doubleBackToExit()
    {
        if (doubleBackToExitPressedOnce)
        {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true

        Toast.makeText(
                this,
                resources.getString(R.string.please_click_back_again_to_exit),
                Toast.LENGTH_SHORT
        ).show()

        @Suppress("DEPRECATION")
        android.os.Handler().postDelayed({doubleBackToExitPressedOnce = false},2000)
    }

}