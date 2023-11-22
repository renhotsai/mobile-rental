package com.hy.group3_project.Models

import com.hy.group3_project.Enums.EditAccountStatus
import com.hy.group3_project.Enums.EditPasswordStatus
import com.hy.group3_project.Enums.LoginStatus
import com.hy.group3_project.Enums.Roles
import java.io.Serializable


class User(
    var firstName: String,
    var lastName: String,
    var email: String,
    var role: Roles,
    private var password: String
): Serializable {
    var isLogin: Boolean = false
    fun login(email: String, password: String): LoginStatus {
        if (this.email == email && this.password == password) {
            this.isLogin = true
            return LoginStatus.Success
        } else {
            this.isLogin = false
            return LoginStatus.PasswordError
        }
    }

    fun logout(): Boolean {
        this.isLogin = false
        return true
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

    override fun toString(): String {
        return "User(firstName='$firstName', lastName='$lastName', email='$email', password='$password')"
    }
}