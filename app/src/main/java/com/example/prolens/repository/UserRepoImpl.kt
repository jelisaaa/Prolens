package com.example.prolens.repository

import com.example.prolens.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class UserRepoImpl : UserRepo {

    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()

    val ref : DatabaseReference = database.getReference("Users")
    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    callback(true,"Login success")
                }else{
                    callback(false, "${it.exception?.message}")


                }
            }
    }

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if ( it.isSuccessful){
                    callback(true,"Registration success","${auth.currentUser?.uid}")
                }else{
                    callback(true,"${it.exception?.message}","")
                }
            }
    }

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    callback(true,"Reset email to ")
                }else{
                    callback(false,"")
                }
            }
    }

    override fun addUserToDatabase(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).setValue(model).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"User registered successfully")
            }else{
                callback(false,"${it.exception?.message}")
            }
        }
    }

}