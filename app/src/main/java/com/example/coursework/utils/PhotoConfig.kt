package com.example.coursework.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PhotoConfig(image: ByteArray, fileName: String) {
    private val requestFile: RequestBody
    val body: MultipartBody.Part
    init {
        requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)

        body =
            MultipartBody.Part.createFormData("file", fileName, requestFile)
    }
}