package com.hy.group3_project.models.users

import android.util.Log
import com.hy.group3_project.models.enums.EditAccountStatus
import com.hy.group3_project.models.enums.EditPasswordStatus
import com.hy.group3_project.models.enums.LoginStatus
import com.hy.group3_project.models.enums.ResponseEnum
import com.hy.group3_project.models.enums.Roles
import com.hy.group3_project.models.properties.Property
import java.io.Serializable


class User(
    var firstName: String,
    var lastName: String,
    var email: String,
    var role: Roles,
    private var password: String

): Serializable {
    private var propertyList:MutableList<Property> = mutableListOf()

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
    fun showList(): MutableList<Property> {
        return this.propertyList
    }

    fun addList(item: Property): ResponseEnum {
        return try {
            if(this.propertyList ==null){
                this.propertyList = mutableListOf<Property>()
            }
            item.isFavorite = true
            this.propertyList.add(item)
            ResponseEnum.Success
        } catch (ex:Exception){
            Log.e("Tenant","$ex")
            ResponseEnum.Fail
        }
    }

    fun removeList(propertyId:String): ResponseEnum {
        try{
            val property = propertyList.find { it.id == propertyId }
            val index = propertyList.indexOf(property)
            this.propertyList.removeAt(index)
            return ResponseEnum.Success
        }catch (ex:Exception){
            Log.e("Tenant","$ex")
            return ResponseEnum.Fail
        }
    }

    override fun toString(): String {
        return "User(firstName='$firstName', lastName='$lastName', email='$email', role=$role, password='$password', propertyList=$propertyList)"
    }
}