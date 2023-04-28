package com.example.coursework.model.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostModel(
    @SerializedName("id_post")
    val idPost: Int? = null,

    @SerializedName("photo")
    val photo: String,

    @SerializedName("label")
    val label: String,

    @SerializedName("id_user")
    val idUser: Int,

    @SerializedName("likes")
    val likes: Int
) : Serializable
