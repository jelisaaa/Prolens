package com.example.prolens.viewmodel

import androidx.lifecycle.ViewModel
import com.example.prolens.model.UserModel
import com.example.prolens.repository.UserRepoImpl

class UserViewModel (val repo: UserRepoImpl) : ViewModel(){

    fun login(email: String,password: String,
              callback:(Boolean, String) -> Unit){
        repo.login(email,password,callback)
    }

    fun register(email: String,password: String,
                 callback: (Boolean, String,String) -> Unit){
        repo.register(email,password,callback)

    }

    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ){
        repo.forgetPassword(email,callback)
    }

    fun addUserToDatabase(userId: String, model: UserModel,
                          callback: (Boolean, String) -> Unit
    ){
        repo.addUserToDatabase(userId,model,callback)
    }
}