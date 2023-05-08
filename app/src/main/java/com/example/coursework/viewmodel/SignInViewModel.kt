package com.example.coursework.viewmodel

import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.model.entity.UserModel
import com.example.coursework.model.liveuser.MyAccount
import com.example.coursework.model.repository.Repository
import com.example.coursework.utils.error.NetworkException
import com.example.coursework.utils.error.ServerException
import com.example.coursework.utils.error.ValidateException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Response
import java.net.ProtocolException
import javax.inject.Inject

@Suppress("DEPRECATION")
class SignInViewModel @Inject constructor(
    private val repository: Repository,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    suspend fun signIn(email: String, password: String): Boolean {
        if (!isConnected()) throw NetworkException()

        if (isNotValid(email) || isNotValid(password)) throw ValidateException()

        try {
            val response = getUser(email, password)

            if (response.await().isSuccessful && response.await().body() != null) {
                MyAccount.user.postValue(response.await().body())
                return true
            } else return false

        } catch (e: ServerException) {
            throw ServerException()
        } catch (e: ProtocolException) {
            throw ServerException()
        }
    }

    private fun getUser(email: String, password: String): Deferred<Response<UserModel?>> {
        return viewModelScope.async(Dispatchers.IO) {
            repository.getUser(email = email, password = password.toInt())
        }
    }

    private fun isConnected(): Boolean {
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

    private fun isNotValid(data: String): Boolean = data.isEmpty()
}