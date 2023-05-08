package com.example.coursework.viewmodel

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.model.entity.UserModel
import com.example.coursework.model.repository.Repository
import com.example.coursework.utils.Constants
import com.example.coursework.utils.PhotoConfig
import com.example.coursework.utils.error.ServerException
import com.example.coursework.utils.error.ValidateException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.MultipartBody
import retrofit2.Response
import java.lang.IllegalStateException
import java.net.ProtocolException
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val imageUri = MutableLiveData<Uri>()

    suspend fun signUp(name: String, email: String, password: String, image: ByteArray): Boolean {
        if (validate(name) && validateEmail(email) && validate(password)) {
            try {
                val fileName: String = setFileName(name, password)

                if (uploadPhoto(image, fileName))
                    return postRequest(fileName, name, email, password)

                else throw ServerException()
            } catch (e: ServerException) {
                throw ServerException()
            } catch (e: ProtocolException) {
                throw ServerException()
            }
        } else {
            throw ValidateException()
        }
    }

    private suspend fun postRequest(
        fileName: String,
        name: String,
        email: String,
        password: String
    ): Boolean {
        val imageUrl: String = Constants.IMAGE_URL + fileName

        try {
            val response =
                postUser(name, email, password, imageUrl)

            if (response.await().isSuccessful) {
                if (response.await().body() == 1)
                    return true
                else if (response.await().body() == -1)
                    return false
            } else throw ServerException()
        } catch (e: ServerException) {
            throw ServerException()
        }
        return false
    }

    private fun postUser(
        name: String,
        email: String,
        password: String,
        image: String
    ): Deferred<Response<Int>> {
        return viewModelScope.async(Dispatchers.IO) {
            repository.postUser(
                UserModel(
                    name = name, email = email, password = password.toInt(), avatarPhoto = image
                )
            )
        }
    }

    private suspend fun uploadPhoto(image: ByteArray, fileName: String): Boolean {
        val photo = PhotoConfig(image, fileName)

        val photoResponse = sendPhotoRequest(photo.body)

        if (photoResponse.await().body().isNullOrEmpty())
            throw ServerException()

        if (photoResponse.await().isSuccessful
            && photoValidate(photoResponse.await().body()!!)
        )
            return true

        return false
    }

    private fun sendPhotoRequest(
        photo: MultipartBody.Part
    ): Deferred<Response<Map<String, String>>> {
        return viewModelScope.async(Dispatchers.IO) {
            repository.uploadPhoto(photo)
        }
    }

    private fun photoValidate(photo: Map<String, String>): Boolean {
        return !photo["status"].isNullOrEmpty() && photo["status"] == "successfully"
    }

    private fun validate(data: String): Boolean {
        return data.isNotEmpty()
    }

    private fun validateEmail(email: String): Boolean {
        return "@" in email
    }

    fun openFile(contentResolver: ContentResolver): ByteArray {
        if (imageUri.value == null) {
            throw Exception()
        } else {
            val data =
                contentResolver.openInputStream(imageUri.value!!)?.use {
                    it.readBytes()
                } ?: throw IllegalStateException("Can't open input stream")
            return data
        }
    }

    private fun setFileName(name: String, password: String): String {
        return "account_${name}_${password}.png"
    }
}

//                val photo = PhotoConfig(image, fileName)
//
//                val photoResponse = sendPhotoRequest(photo.body)
//
//                if (photoResponse.await().body().isNullOrEmpty())
//                    throw ServerException()
//
//                if (photoResponse.await().isSuccessful && photoValidate(
//                        photoResponse.await().body()!!)
//                ) {


//                    val imageUrl: String = Constants.IMAGE_URL + fileName
//
//                    val response =
//                        postUser(name, email, password, imageUrl)
//
//                    if (response.await().isSuccessful) {
//                        if (response.await().body() == 1)
//                            return true
//                        else if (response.await().body() == -1)
//                            return false
//                    } else throw ServerException()



//            viewModelScope.launch(Dispatchers.IO) {
//                val response =
//                    repository.postUser(UserModel(name = name, email = email, password = password.toInt()))
//                if (response.isSuccessful && response.body() == 1)
//                    return true
//            }

