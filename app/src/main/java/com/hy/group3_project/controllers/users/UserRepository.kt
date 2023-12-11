package com.hy.group3_project.controllers.users

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.hy.group3_project.models.users.User

class UserRepository(private val context: Context) {
    private val TAG = this.javaClass.simpleName
    private val db = Firebase.firestore

    private val COLLECTION_USERS = "Users"
    private var FIELD_ID = "id"
    private var FIELD_USER_FIRST_NAME = "firstName"
    private var FIELD_USER_LAST_NAME = "lastName"
    private var FIELD_USER_EMAIL = "email"
    private var FIELD_USER_ROLE = "role"
    private var FIELD_USER_PASSWORD = "password"

    var userAll : MutableLiveData<List<User>> = MutableLiveData<List<User>>()

    fun addUserToDB(user: User) {
        try {
            Log.d(TAG, "addUserToDB: $user")
            val data: MutableMap<String, Any> = HashMap()
            data[FIELD_ID] = user.id
            data[FIELD_USER_FIRST_NAME] = user.firstName
            data[FIELD_USER_LAST_NAME] = user.lastName
            data[FIELD_USER_ROLE] = user.role

            db.collection(COLLECTION_USERS)
                .document(user.id)
                .set(data)
                .addOnSuccessListener { docRef ->
                    Log.d(
                        TAG,
                        "addUserToDB: Document successfully added with ID : $docRef"
                    )
                }
                .addOnFailureListener { ex ->
                    Log.e(
                        TAG,
                        "addUserToDB: Exception occurred while adding a document : $ex",
                    )
                }
        } catch (ex : java.lang.Exception){
            Log.d(
                TAG,
                "addUserToDB: Couldn't perform insert on Countries collection due to exception $ex"
            )
        }
    }





}