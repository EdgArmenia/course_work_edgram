package com.example.coursework.model.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserModel(
    @SerializedName("id_user")
    val idUser: Int = 0,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("avatar_photo")
    val avatarPhoto: String,

    @SerializedName("password")
    val password: Int
) : Serializable
