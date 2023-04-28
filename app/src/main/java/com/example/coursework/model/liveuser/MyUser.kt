package com.example.coursework.model.liveuser

import androidx.lifecycle.MutableLiveData
import com.example.coursework.model.entity.UserModel

object MyUser {
    val user = MutableLiveData<UserModel>()
}