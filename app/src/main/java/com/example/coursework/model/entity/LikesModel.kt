package com.example.coursework.model.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LikesModel(
    @SerializedName("id_user")
    val idUser: Int,

    @SerializedName("id_post")
    val idPost: Int
) : Serializable
