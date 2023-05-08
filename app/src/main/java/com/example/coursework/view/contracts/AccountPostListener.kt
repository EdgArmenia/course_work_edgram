package com.example.coursework.view.contracts

import com.example.coursework.model.entity.PostModel

interface AccountPostListener : PostsListener {
    fun onDeletePost(post: PostModel)
    fun onClickEditPost(post: PostModel)
}