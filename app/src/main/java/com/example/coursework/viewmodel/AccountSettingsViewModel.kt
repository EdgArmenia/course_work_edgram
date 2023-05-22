package com.example.coursework.viewmodel

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.model.entity.UserModel
import com.example.coursework.model.liveuser.MyAccount
import com.example.coursework.model.repository.Repository
import com.example.coursework.utils.Constants
import com.example.coursework.utils.PhotoConfig
import com.example.coursework.utils.error.ServerException
import com.example.coursework.utils.error.ValidateException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withTimeout
import retrofit2.Response
import java.lang.IllegalStateException
import java.lang.NullPointerException
import java.net.ProtocolException
import javax.inject.Inject

class AccountSettingsViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    val imageUri = MutableLiveData<Uri>()

    suspend fun putUser(name: String, email: String, password: String, image: ByteArray?): Boolean {
        if (validate(name) && validateEmail(email) && validate(password)) {
            try {
                val fileName: String =
                    if (image != null) setFileName(name, password)
                    else MyAccount.user.value!!.avatarPhoto.substring(31)

                if (image != null)
                    uploadPhoto(image, fileName)

                try {
                    return putRequest(fileName, name, email, password)
                } catch (e: ServerException) {
                    throw ServerException()
                } catch (e: ProtocolException) {
                    throw ServerException()
                }

            } catch (e: ServerException) {
                throw ServerException()
            } catch (e: ProtocolException) {
                throw ServerException()
            }
        } else throw ValidateException()
    }

    private suspend fun putRequest(
        fileName: String,
        name: String,
        email: String,
        password: String
    ): Boolean {
        val imageUrl: String = Constants.IMAGE_URL + fileName

        val response = requestUser {
            withTimeout(1500L) {
                repository.putUser(
                    UserModel(
                        idUser = MyAccount.user.value!!.idUser,
                        name = name,
                        email = email,
                        password = password.toInt(),
                        avatarPhoto = imageUrl
                    )
                )
            }
        }

        if (response.await().isSuccessful) {
            if (response.await().body() == 1) {
                MyAccount.user.postValue(
                    UserModel(
                        idUser = MyAccount.user.value!!.idUser,
                        name = name,
                        email = email,
                        password = password.toInt(),
                        avatarPhoto = imageUrl
                    )
                )
                return true
            } else if (response.await().body() == -1)
                return false
        } else throw ServerException()

        return false
    }

    private suspend fun uploadPhoto(image: ByteArray, fileName: String) {
        val photo = PhotoConfig(image, fileName)

        val photoResponse = requestPhoto { repository.uploadPhoto(photo.body) }

        if (photoResponse.await().body().isNullOrEmpty())
            throw ServerException()

        if (!(photoResponse.await().isSuccessful && photoValidate(
                photoResponse.await().body()!!
            ))
        ) throw ServerException()
    }

//    private suspend fun getUser(email: String, password: String) {
//        val response =
//            requestGetUser { repository.getUser(email = email, password = password.toInt()) }
//
//        if (response.await().isSuccessful && response.await().body() != null)
//            MyAccount.user.value = response.await().body()
//    }

    suspend fun deleteUser(): Boolean {
        val response = requestUser {
            repository.deleteUser(MyAccount.user.value!!.idUser)
        }

        if (response.await().isSuccessful)
            return response.await().body() == 1

        return false
    }

    fun openFile(contentResolver: ContentResolver): ByteArray {
        if (imageUri.value == null) {
            throw NullPointerException()
        } else {
            val data =
                contentResolver.openInputStream(imageUri.value!!)?.use {
                    it.readBytes()
                } ?: throw IllegalStateException("Can't open input stream")
            return data
        }
    }

    private suspend fun requestPhoto(
        query: suspend () -> Response<Map<String, String>>
    ): Deferred<Response<Map<String, String>>> {
        return withTimeout(1500L) {
            viewModelScope.async(Dispatchers.IO) {
                query()
            }
        }
    }

//    private fun requestGetUser(
//        query: suspend () -> Response<UserModel?>
//    ): Deferred<Response<UserModel?>> {
//        return viewModelScope.async(Dispatchers.IO) {
//            query()
//        }
//    }

    private suspend fun requestUser(
        query: suspend () -> Response<Int>
    ): Deferred<Response<Int>> {
        return withTimeout(1500L) {
            viewModelScope.async(Dispatchers.IO) {
                query()
            }
        }
    }

    private fun setFileName(name: String, password: String): String {
        return "account_${name}_${password}.png"
    }

    private fun validate(data: String): Boolean {
        return data.isNotEmpty()
    }

    private fun validateEmail(email: String): Boolean =
        "@mail.ru" in email || "@gmail.com" in email || "@yandex.ru" in email || "@ya.ru" in email

    private fun photoValidate(photo: Map<String, String>): Boolean {
        return !photo["status"].isNullOrEmpty() && photo["status"] == "successfully"
    }
}