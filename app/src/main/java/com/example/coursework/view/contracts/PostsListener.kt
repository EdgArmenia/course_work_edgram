package com.example.coursework.view.contracts

import com.example.coursework.model.entity.PostModel

interface PostsListener {
    fun onClickShowUsersLiked(post: PostModel)
    fun onClickLike(idPost: Int)
}