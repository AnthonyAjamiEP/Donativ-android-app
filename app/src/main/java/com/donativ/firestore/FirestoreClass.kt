package com.donativ.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.donativ.models.Product
import com.donativ.models.RequestedProduct
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.donativ.models.User
import com.donativ.ui.activities.*
import com.donativ.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.donativ.ui.activities.AddProductActivity
import java.util.ArrayList

//  A custom class where we will add the operations performed for the FireStore database.

class FirestoreClass {

    // Access a Cloud Firestore instance.
    private val mFireStore = FirebaseFirestore.getInstance()

//      A function to make an entry of the registered user in the FireStore database.
    fun registerUser(activity: RegisterActivity, userInfo: User)
    {

        // The "users" is collection name. If the collection is already created then it will not create the same one again but add to it.
        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressdialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String
    {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity)
    {

        // Here we pass the collection name from which we want the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(Constants.DONATIV_PREFERENCES,Context.MODE_PRIVATE)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                // Key:Value logged_in_username: first name + last name
                // can be used later in app to say Hello firstname + lastname !
                editor.putString(Constants.LOGGED_IN_USERNAME, "${user.firstName} ${user.lastName}")
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressdialog()
                    }
                    is LoginActivity -> {
                        activity.hideProgressdialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting User details.",
                    e
                )
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String,Any>)
    {
        mFireStore.collection(Constants.USERS).document(getCurrentUserID()).update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity -> {
                        // hide the progress dialog on success and send the user to main activity after profile completion
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener{ e->
                when(activity){
                    is UserProfileActivity -> {
                        // hide the progress dialog when there is an error and log the error type
                        activity.hideProgressdialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String)
    {
        // create name fot the file on the storage. Name consists of User_Profile_Image + time in ms + .file extension
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                            imageType + System.currentTimeMillis() + "."
                                    + Constants.getFileExtension(
                                    activity,
                                    imageFileURI)
                        )
        // uploading the image to storage. NOT TO CURRENT USER'S DOCUMENT
        sRef.putFile(imageFileURI!!)
                .addOnSuccessListener { taskSnapshot ->
                    // just logging the url of the image
                    Log.e(
                            "FireBase Image URL",
                            taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                    )
                    // calling function to print toast passing it image url to be included in toast
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener { uri ->
                                Log.e("Downloadable Image URL", uri.toString())
                                when (activity) {
                                    is UserProfileActivity -> {
                                        activity.imageUploadSuccess(uri.toString())
                                    }
                                    is AddProductActivity -> {
                                        activity.imageUploadSuccess(uri.toString())
                                    }
                                }
                            }
                }
                // logging the error if failed
                .addOnFailureListener { exception ->
                    when (activity) {
                        is UserProfileActivity -> {
                            activity.hideProgressdialog()
                        }
                        is AddProductActivity -> {
                            activity.hideProgressdialog()
                        }
                    }

                    Log.e(
                            activity.javaClass.simpleName,
                            exception.message,
                            exception
                    )
                }
    }

    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product)
    {
        mFireStore.collection(Constants.PRODUCTS)
                .document()
                .set(productInfo, SetOptions.merge())
                .addOnSuccessListener {
                    activity.productUploadSuccess()
                }
                .addOnFailureListener { e ->
                    activity.hideProgressdialog()
                    Log.e(
                            activity.javaClass.simpleName,
                            "Error while uploading product details.",
                            e
                    )
                }
    }
    fun uploadRequestedProductDetails(activity: AddRequestedProductActivity, productInfo: RequestedProduct)
    {
        mFireStore.collection(Constants.REQUESTED_PRODUCTS)
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.requestedProductUploadSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressdialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading product details.",
                    e
                )
            }
    }

    fun getProductsList(activity: Activity)
    {
        when (activity) {
            is ViewListedProductsActivity -> {
                mFireStore.collection(Constants.PRODUCTS)
                        // ONLY SHOW CURRENT USER PRODUCTS
                        .whereEqualTo(Constants.USER_ID, getCurrentUserID())
                        .get()
                        .addOnSuccessListener { document ->
                            Log.e("Products list", document.documents.toString())
                            val productsList: ArrayList<Product> = ArrayList()
                            for (i in document.documents) {
                                val product = i.toObject(Product::class.java)
                                product!!.product_id = i.id
                                productsList.add(product)
                            }
                            activity.successProductsListFromFireStore(productsList)
                        }
                        .addOnFailureListener {
                            activity.hideProgressdialog()
                        }
            }
            is AllProductsActivity -> {
                mFireStore.collection(Constants.PRODUCTS)
                    // NO FILTERING -> ALL PRODUCTS SHOWN
                    .get()
                    .addOnSuccessListener { document ->
                        Log.e("Products list", document.documents.toString())
                        val productsList: ArrayList<Product> = ArrayList()
                        for (i in document.documents) {
                            val product = i.toObject(Product::class.java)
                            product!!.product_id = i.id
                            productsList.add(product)
                        }
                        activity.successAllProductsListFromFireStore(productsList)
                    }
                    .addOnFailureListener {
                        activity.hideProgressdialog()
                    }
            }
            is ViewRequestedProductsActivity -> {
                mFireStore.collection(Constants.REQUESTED_PRODUCTS)
                    // ONLY SHOW CURRENT USER PRODUCTS
                    .whereEqualTo(Constants.USER_ID, getCurrentUserID())
                    .get()
                    .addOnSuccessListener { document ->
                        Log.e("Products list", document.documents.toString())
                        val productsList: ArrayList<RequestedProduct> = ArrayList()
                        for (i in document.documents) {
                            val product = i.toObject(RequestedProduct::class.java)
                            product!!.product_id = i.id
                            productsList.add(product)
                        }
                        activity.successRequestedProductsListFromFireStore(productsList)
                    }
                    .addOnFailureListener {
                        activity.hideProgressdialog()
                    }
            }
            is ViewPeopleRequestedProductsActivity -> {
                mFireStore.collection(Constants.REQUESTED_PRODUCTS)
                    // NO FILTERING -> ALL PRODUCTS SHOWN
                    .get()
                    .addOnSuccessListener { document ->
                        Log.e("Products list", document.documents.toString())
                        val productsList: ArrayList<RequestedProduct> = ArrayList()
                        for (i in document.documents) {
                            val product = i.toObject(RequestedProduct::class.java)
                            product!!.product_id = i.id
                            productsList.add(product)
                        }
                        activity.successPeopleRequestedProductsListFromFireStore(productsList)
                    }
                    .addOnFailureListener {
                        activity.hideProgressdialog()
                    }
            }
        }
    }

    fun getFilteredProductsList(activity: AllProductsActivity, filteredText: String)
    {
        mFireStore.collection(Constants.PRODUCTS)
                .whereEqualTo(Constants.TITLE, filteredText)
                .get()
                .addOnSuccessListener { document ->
                    Log.e("Products list", document.documents.toString())
                    val productsList: ArrayList<Product> = ArrayList()
                    for (i in document.documents) {
                        val product = i.toObject(Product::class.java)
                        product!!.product_id = i.id
                        productsList.add(product)
                    }
                    activity.successFilteredProductsListFromFireStore(productsList)
                }
                .addOnFailureListener {
                    activity.hideProgressdialog()
                }
    }


    fun deleteProduct(activity: ViewListedProductsActivity, productId: String) {

        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                // Notify the success result to the base class.
                activity.productDeleteSuccess()
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                activity.hideProgressdialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while deleting the product.",
                    e
                )
            }
    }
    fun deleteRequestedProduct(activity: ViewRequestedProductsActivity, productId: String) {

        mFireStore.collection(Constants.REQUESTED_PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                // Notify the success result to the base class.
                activity.requestedProductDeleteSuccess()
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                activity.hideProgressdialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while deleting the product.",
                    e
                )
            }
    }

    fun getUserProductDetails(activity: UserProductDetailsActivity, productId: String) {

        // The collection name for PRODUCTS
        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.toString())

                val product = document.toObject(Product::class.java)!!
                var mProductUserID: String = product.user_id

                activity.productDetailsSuccess(product)
            }
                        .addOnFailureListener { e->
                            activity.hideProgressdialog()

                            Log.e(activity.javaClass.simpleName, "Error while getting the product details.", e)
                        }
            }
    fun getRequestedProductDetails(activity: RequestedProductDetailsActivity, productId: String) {

        // The collection name for PRODUCTS
        mFireStore.collection(Constants.REQUESTED_PRODUCTS)
            .document(productId)
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.toString())

                val requestedProduct = document.toObject(RequestedProduct::class.java)!!
                var mProductUserID: String = requestedProduct.user_id

                activity.requestedProductDetailsSuccess(requestedProduct)
            }
            .addOnFailureListener { e->
                activity.hideProgressdialog()

                Log.e(activity.javaClass.simpleName, "Error while getting the product details.", e)
            }
    }

    fun getAllProductDetails(activity: AllProductDetailsActivity, productId: String)
    {
        mFireStore.collection(Constants.PRODUCTS)
                .document(productId)
                .get() // Will get the document snapshots.
                .addOnSuccessListener { document ->
                    Log.e(activity.javaClass.simpleName, document.toString())

                    val product = document.toObject(Product::class.java)!!
                    var mProductUserID:String = product.user_id

                    mFireStore.collection(Constants.USERS)
                            .document(mProductUserID)
                            .get()
                            .addOnSuccessListener { document ->
                                val user = document.toObject(User::class.java)!!
                                activity.productDetailsSuccess(product,user)
                            }
                            .addOnFailureListener { e->
                                activity.hideProgressdialog()

                                Log.e(activity.javaClass.simpleName, "Error while getting the product details.", e)
                            }
                }
    }
    fun getPeopleRequestedProductDetails(activity: PeopleRequestedProductDetailsActivity, productId: String)
    {
        mFireStore.collection(Constants.REQUESTED_PRODUCTS)
            .document(productId)
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.toString())

                val product = document.toObject(RequestedProduct::class.java)!!
                var mProductUserID:String = product.user_id

                mFireStore.collection(Constants.USERS)
                    .document(mProductUserID)
                    .get()
                    .addOnSuccessListener { document ->
                        val user = document.toObject(User::class.java)!!
                        activity.RequestedProductDetailsSuccess(product,user)
                    }
                    .addOnFailureListener { e->
                        activity.hideProgressdialog()

                        Log.e(activity.javaClass.simpleName, "Error while getting the product details.", e)
                    }
            }
    }
}