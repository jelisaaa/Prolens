package com.example.prolens.repository

import com.example.prolens.model.UserModel

interface UserRepo {

    fun login(email: String, password: String,
              callback: (Boolean, String) -> Unit)

    fun register(email: String, password: String,
                 callback:(Boolean, String, String) -> Unit)

    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    )

    fun addUserToDatabase(
        userId: String,
        model: UserModel, callback: (Boolean, String) -> Unit
    )


}