package com.example.coursework.utils

import com.example.coursework.model.entity.LikesModel
import com.example.coursework.model.entity.PostModel

data class PostAndLikesData(val posts: List<PostModel>, val likes: List<LikesModel>)
