package com.hy.group3_project.models.users

import android.util.Log
import com.hy.group3_project.models.enums.EditAccountStatus
import com.hy.group3_project.models.enums.EditPasswordStatus
import com.hy.group3_project.models.enums.ResponseEnum
import java.io.Serializable


class User : Serializable {

    lateinit var id: String
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var role: String
    private var propertyList: MutableList<String> = mutableListOf()

    constructor()
    constructor(
        id: String,
        firstName: String,
        lastName: String,
        role: String,
        ){
        this.id= id
        this.firstName = firstName
        this.lastName = lastName
        this.role = role
    }

    fun changePassword(
        currPassword: String,
        newPassword: String,
        confirmPassword: String,
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
//        if (currPassword != this.password) {
//            return EditPasswordStatus.CurrentPasswordWrong
//        }
//        if (newPassword != confirmPassword) {
//            return EditPasswordStatus.ConfirmPasswordDifferent
//        }
//
//        //save password
//        this.password = newPassword
//        return EditPasswordStatus.Success
        return EditPasswordStatus.Success
    }

    fun changeAcctInfo(firstName: String, lastName: String, email: String): EditAccountStatus {
        if (!firstName.isNullOrEmpty()) {
            this.firstName = firstName
        }
        if (!lastName.isNullOrEmpty()) {
            this.lastName = lastName
        }
//        if (!email.isNullOrEmpty()) {
//            this.email = email
//        }
        return EditAccountStatus.Success
    }

    fun showList(): MutableList<String> {
        return this.propertyList
    }

    fun addList(item: String): ResponseEnum {
        return try {
            this.propertyList.add(item)
            ResponseEnum.Success
        } catch (ex: Exception) {
            Log.e("Tenant", "$ex")
            ResponseEnum.Fail
        }
    }

    fun removeList(propertyId: String): ResponseEnum {
        try {
            val property = propertyList.find { it == propertyId }
            val index = propertyList.indexOf(property)
            this.propertyList.removeAt(index)
            return ResponseEnum.Success
        } catch (ex: Exception) {
            Log.e("Tenant", "$ex")
            return ResponseEnum.Fail
        }
    }

    override fun toString(): String {
        return "User(firstName='$firstName', lastName='$lastName', propertyList=$propertyList)"
    }
}