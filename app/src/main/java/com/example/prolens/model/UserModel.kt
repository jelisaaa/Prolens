package com.example.prolens.model

data class UserModel(
    val userId: String ="",
    val email: String ="" ,
    val firstName: String ="",
    val lastName: String ="",
    val dob: String ="",




){
    fun toMap() : Map<String, Any?>{
        return mapOf(
            "userId" to userId,
            "email" to email,
            "firstName" to firstName,
            "lastName" to lastName,
            "dob" to dob,


        )

    }
}
