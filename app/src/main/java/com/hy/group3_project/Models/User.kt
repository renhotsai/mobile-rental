package com.hy.group3_project.Models

import android.util.Log
import com.hy.group3_project.Enums.EditAccountStatus
import com.hy.group3_project.Enums.EditPasswordStatus
import com.hy.group3_project.Enums.LoginStatus
import com.hy.group3_project.Enums.ResponseEnum
import com.hy.group3_project.Enums.Roles
import com.hy.group3_project.Listing
import java.io.Serializable


class User(
    var firstName: String,
    var lastName: String,
    var email: String,
    var role: Roles,
    private var password: String

): Serializable {
    private var listingList:MutableList<Listing> = mutableListOf()

    fun login(email: String, password: String): LoginStatus {
        if (this.email == email && this.password == password) {
            return LoginStatus.Success
        } else {
            return LoginStatus.PasswordError
        }
    }
    fun changePassword(
        currPassword: String,
        newPassword: String,
        confirmPassword: String
    ): EditPasswordStatus {
        //check password is empty or null
        if (currPassword.isNullOrEmpty()) {
            return EditPasswordStatus.CurrentPasswordEmpty
        }
        if (newPassword.isNullOrEmpty()) {
            return EditPasswordStatus.NewPasswordEmpty
        }
        if (confirmPassword.isNullOrEmpty()) {
            return EditPasswordStatus.ConfirmPasswordEmpty
        }

        //check password
        if (currPassword != this.password) {
            return EditPasswordStatus.CurrentPasswordWrong
        }
        if (newPassword != confirmPassword) {
            return EditPasswordStatus.ConfirmPasswordDifferent
        }

        //save password
        this.password = newPassword
        return EditPasswordStatus.Success
    }

    fun changeAcctInfo(firstName: String, lastName: String, email: String): EditAccountStatus {
        if (!firstName.isNullOrEmpty()) {
            this.firstName = firstName
        }
        if (!lastName.isNullOrEmpty()) {
            this.lastName = lastName
        }
        if (!email.isNullOrEmpty()) {
            this.email = email
        }
        return EditAccountStatus.Success
    }
    fun showListingList(): MutableList<Listing> {
        return this.listingList
    }

    fun addFavoriteList(item: Listing): ResponseEnum {
        return try {
            this.listingList.add(item)
            ResponseEnum.Success
        } catch (ex:Exception){
            Log.e("Tenant","$ex")
            ResponseEnum.Fail
        }
    }

    fun removeFavoriteList(pos:Int): ResponseEnum {
        try{
            this.listingList.removeAt(pos)
            return ResponseEnum.Success
        }catch (ex:Exception){
            Log.e("Tenant","$ex")
            return ResponseEnum.Fail
        }
    }

    override fun toString(): String {
        return "User(firstName='$firstName', lastName='$lastName', email='$email', password='$password')"
    }
}