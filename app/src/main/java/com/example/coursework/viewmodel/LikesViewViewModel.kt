package com.example.coursework.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.model.entity.PostModel
import com.example.coursework.model.entity.UserModel
import com.example.coursework.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class LikesViewViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val usersLiked = MutableLiveData<List<UserModel>>()

    fun getUsersLiked(post: PostModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = withTimeout(1500L) { repository.getUsersWithLike(post.idPost) }

            if (response.isSuccessful) {
                usersLiked.postValue(response.body())
            }
        }
    }
}