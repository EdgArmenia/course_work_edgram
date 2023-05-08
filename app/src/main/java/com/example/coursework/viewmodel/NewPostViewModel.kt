package com.example.coursework.viewmodel

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.model.entity.PostModel
import com.example.coursework.model.liveuser.MyAccount
import com.example.coursework.model.repository.Repository
import com.example.coursework.utils.Constants
import com.example.coursework.utils.PhotoConfig
import com.example.coursework.utils.error.ServerException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Response
import java.lang.IllegalStateException
import java.lang.NullPointerException
import java.net.ProtocolException
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class NewPostViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val imageUri = MutableLiveData<Uri>()
    val post = MutableLiveData<PostModel?>()
    val isEdit = MutableLiveData<Boolean>()

    @SuppressLint("SimpleDateFormat")
    suspend fun postPost(label: String, image: ByteArray?): Boolean {
        try {
            val fileName: String = setFileName()
            val currentDate = SimpleDateFormat("dd/MM/yyyy").format(Date())

            return sendRequest(fileName, image) {
                postRequest(fileName, label, currentDate)
            }

        } catch (e: ServerException) {
            throw ServerException()
        } catch (e: ProtocolException) {
            throw ServerException()
        }
    }


    @SuppressLint("SimpleDateFormat")
    suspend fun putPost(label: String, image: ByteArray?): Boolean {
        try {
            val fileName: String =
                if (image != null) setFileName() else post.value!!.photo.substring(31)
            val currentDate = SimpleDateFormat("dd/MM/yyyy").format(Date())

            return sendRequest(fileName, image) {
                putRequest(fileName, label, currentDate)
            }

        } catch (e: ServerException) {
            throw ServerException()
        } catch (e: ProtocolException) {
            throw ServerException()
        }
    }

    private suspend fun sendRequest(
        fileName: String,
        image: ByteArray?,
        request: suspend () -> Boolean
    ): Boolean {
        try {
            if (image != null) {
                if (uploadPhoto(image, fileName)) {
                    try {
                        return request()
                    } catch (e: ServerException) {
                        throw ServerException()
                    }
                } else throw ServerException()
            } else {
                try {
                    return request()
                } catch (e: ServerException) {
                    throw ServerException()
                }
            }
        } catch (e: ServerException) {
            throw ServerException()
        } catch (e: ProtocolException) {
            throw ServerException()
        }
    }

    private fun requestPost(
        query: suspend () -> Response<Int>
    ): Deferred<Response<Int>> {
        return viewModelScope.async(Dispatchers.IO) {
            query()
        }
    }

    private fun requestPhoto(
        query: suspend () -> Response<Map<String, String>>
    ): Deferred<Response<Map<String, String>>> {
        return viewModelScope.async(Dispatchers.IO) {
            query()
        }
    }

    private suspend fun postRequest(fileName: String, label: String, currentDate: String): Boolean {
        val imageUrl: String = Constants.IMAGE_URL + fileName

        val response = requestPost {
            repository.postPost(
                PostModel(
                    label = label,
                    photo = imageUrl,
                    date = currentDate,
                    idUser = MyAccount.user.value?.idUser!!,
                )
            )
        }
//            postPost(label, imageUrl, currentDate)

        if (response.await().isSuccessful) {
            if (response.await().body() == 1)
                return true
            else if (response.await().body() == -1)
                return false
        } else throw ServerException()

        return false
    }

    private suspend fun putRequest(fileName: String, label: String, currentDate: String): Boolean {
        val imageUrl: String = Constants.IMAGE_URL + fileName

        val response = requestPost {
            repository.putPost(
                PostModel(
                    label = label,
                    photo = imageUrl,
                    idUser = MyAccount.user.value?.idUser!!,
                    date = currentDate,
                    idPost = post.value!!.idPost
                )
            )
        }
//            putPost(label, imageUrl, currentDate)

        if (response.await().isSuccessful) {
            if (response.await().body() == 1)
                return true
            else if (response.await().body() == -1)
                return false
        } else throw ServerException()

        return false
    }



    private suspend fun uploadPhoto(image: ByteArray, fileName: String): Boolean {
        val photo = PhotoConfig(image, fileName)

        val photoResponse = requestPhoto { repository.uploadPhoto(photo.body) }

        if (photoResponse.await().body().isNullOrEmpty())
            throw ServerException()

        if (photoResponse.await().isSuccessful && photoValidate(
                photoResponse.await().body()!!
            )
        )
            return true

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

    private fun setFileName(): String {
        val num = (0 until 100).random()

        return "post_photo_$num.png"
    }

    private fun photoValidate(photo: Map<String, String>): Boolean {
        return !photo["status"].isNullOrEmpty() && photo["status"] == "successfully"
    }
}

//            if (uploadPhoto(image, fileName)) {
//                try {
//                    postRequest(fileName, label, currentDate)
//                } catch (e: ServerException) {
//                    throw ServerException()
//                }
//            } else throw ServerException()

//            if (image != null) {
//                if (uploadPhoto(image, fileName)) {
//                    try {
//                        return putRequest(fileName, label, currentDate)
//                    } catch (e: ServerException) {
//                        throw ServerException()
//                    }
//                } else throw ServerException()
//            } else {
//                try {
//                    return putRequest(fileName, label, currentDate)
//                } catch (e: ServerException) {
//                    throw ServerException()
//                }
//            }

//    private fun postPost(
//        label: String,
//        image: String,
//        date: String
//    ): Deferred<Response<Int>> {
//        return viewModelScope.async(Dispatchers.IO) {
//            repository.postPost(
//                PostModel(
//                    label = label,
//                    photo = image,
//                    idUser = MyAccount.user.value?.idUser!!,
//                    date = date
//                )
//            )
//        }
//    }

//    private fun putPost(
//        label: String,
//        image: String,
//        date: String
//    ): Deferred<Response<Int>> {
//        return viewModelScope.async(Dispatchers.IO) {
//            repository.putPost(
//                PostModel(
//                    label = label,
//                    photo = image,
//                    idUser = MyAccount.user.value?.idUser!!,
//                    date = date,
//                    idPost = post.value!!.idPost
//                )
//            )
//        }
//    }

// For post
//            val photo = PhotoConfig(image, fileName)
//
//            val photoResponse = sendPhotoRequest(photo.body)
//
//            if (photoResponse.await().body().isNullOrEmpty())
//                throw ServerException()
//
//            if (photoResponse.await().isSuccessful && photoValidate(
//                    photoResponse.await().body()!!
//                )
//            ) {
//                val imageUrl: String = Constants.IMAGE_URL + fileName
//
//                val response =
//                    postPost(label, imageUrl, currentDate)
//
//                if (response.await().isSuccessful) {
//                    if (response.await().body() == 1)
//                        return true
//                    else if (response.await().body() == -1)
//                        return false
//                } else throw ServerException()

// For put
//                val photo = PhotoConfig(image, fileName)
//
//                val photoResponse = sendPhotoRequest(photo.body)
//
//                if (photoResponse.await().body().isNullOrEmpty())
//                    throw ServerException()
//
//                if (photoResponse.await().isSuccessful && photoValidate(
//                        photoResponse.await().body()!!
//                    )
//                ) {
//                    val imageUrl: String = Constants.IMAGE_URL + fileName
//
//                    val response =
//                        putPost(label, imageUrl, currentDate)
//
//                    if (response.await().isSuccessful) {
//                        if (response.await().body() == 1)
//                            return true
//                        else if (response.await().body() == -1)
//                            return false
//                    } else throw ServerException()