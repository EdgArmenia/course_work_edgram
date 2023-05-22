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
import kotlinx.coroutines.withTimeout
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

        if (isNotValidEmail(email) || isNotValid(password)) throw ValidateException()

        try {
            val response =
                request { repository.getUser(email = email, password = password.toInt()) }

            if (response.await().isSuccessful && response.await().body() != null) {
                MyAccount.user.value = response.await().body()
                return true
            } else return false

        } catch (e: ServerException) {
            throw ServerException()
        } catch (e: ProtocolException) {
            throw ServerException()
        }
    }

    private fun request(query: suspend () -> Response<UserModel?>): Deferred<Response<UserModel?>> {
        return viewModelScope.async(Dispatchers.IO) {
            withTimeout(1500L) { query() }
        }
    }

    private fun isConnected(): Boolean {
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

    private fun isNotValid(data: String): Boolean = data.isEmpty()

    private fun isNotValidEmail(data: String): Boolean =
        !("@mail.ru" in data || "@gmail.com" in data || "@yandex.ru" in data || "@ya.ru" in data)

}