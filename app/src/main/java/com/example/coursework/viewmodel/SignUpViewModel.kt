package com.example.coursework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.model.entity.UserModel
import com.example.coursework.model.repository.Repository
import com.example.coursework.utils.error.ServerException
import com.example.coursework.utils.error.ValidateException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Response
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    suspend fun signUp(name: String, email: String, password: String): Boolean {
        if (validate(name) && validateEmail(email) && validate(password)) {
            try {
                val response = postUser(name, email, password)

                if (response.await().isSuccessful) {
                    if (response.await().body() == 1)
                        return true
                    else if (response.await().body() == -1)
                        return false
                } else throw ServerException()

            } catch (e: ServerException) {
                throw ServerException()
            }
        } else {
            throw ValidateException()
        }
        return false
    }

    private fun postUser(
        name: String,
        email: String,
        password: String
    ): Deferred<Response<Int>> {
        return viewModelScope.async(Dispatchers.IO) {
            repository.postUser(
                UserModel(
                    name = name, email = email, password = password.toInt()
                )
            )
        }
    }

    private fun validate(data: String): Boolean {
        return data.isNotEmpty()
    }

    private fun validateEmail(email: String): Boolean {
        return "@" in email
    }
}

//            viewModelScope.launch(Dispatchers.IO) {
//                val response =
//                    repository.postUser(UserModel(name = name, email = email, password = password.toInt()))
//                if (response.isSuccessful && response.body() == 1)
//                    return true
//            }

