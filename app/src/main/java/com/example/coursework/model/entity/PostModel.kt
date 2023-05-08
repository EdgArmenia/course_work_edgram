package com.example.coursework.model.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostModel(
    @SerializedName("id_post")
    val idPost: Int = 0,

    @SerializedName("photo")
    val photo: String,

    @SerializedName("label")
    val label: String = "",

    @SerializedName("id_user")
    val idUser: Int,

    @SerializedName("date")
    val date: String,

    @SerializedName("likes")
    var likes: Int = 0,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("avatar_photo")
    val avatarPhoto: String? = null
) : Serializable
