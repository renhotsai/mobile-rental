package com.hy.group3_project.models.enums

enum class EditPasswordStatus {
    Success,
    CurrentPasswordEmpty,
    NewPasswordEmpty,
    ConfirmPasswordEmpty,
    CurrentPasswordWrong,
    ConfirmPasswordDifferent
}