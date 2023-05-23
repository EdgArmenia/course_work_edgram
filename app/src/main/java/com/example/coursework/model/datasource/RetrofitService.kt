package com.example.coursework.model.datasource

import com.example.coursework.model.entity.LikesModel
import com.example.coursework.model.entity.PostModel
import com.example.coursework.model.entity.UserModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface RetrofitService {
    @GET("user/{id_user}/{email}/{password}")
    @Headers("Content-Type: application/json")
    suspend fun getUser(
        @Path("id_user") id_user: Int = 0,
        @Path("email") email: String = "",
        @Path("password") password: Int = 0
    ): Response<UserModel?>

    @GET("post/{id_user}")
    @Headers("Content-Type: application/json")
    suspend fun getPosts(@Path("id_user") idUser: Int): Response<List<PostModel>>

    @GET("likes/{id_post}")
    @Headers("Content-Type: application/json")
    suspend fun getUsersWithLike(@Path("id_post") idPost: Int): Response<List<UserModel>>

    @GET("likes_check/{id_user}")
    @Headers("Content-Type: application/json")
    suspend fun getLikes(@Path("id_user") idUser: Int = 0): Response<List<LikesModel>>

    @POST("user/0/0/0")
    suspend fun postUser(@Body user: UserModel): Response<Int>

    @POST("post/0")
    suspend fun postPost(@Body post: PostModel): Response<Int>

    @POST("likes/0")
    suspend fun postLike(@Body likes: LikesModel): Response<Map<String, String>>

    @PUT("user/0/0/0")
    suspend fun putUser(@Body user: UserModel): Response<Int>

    @PUT("post/0")
    suspend fun putPost(@Body post: PostModel): Response<Int>

    @DELETE("user/{id_user}/0/0")
    suspend fun deleteUser(@Path("id_user") idUser: Int): Response<Int>

    @DELETE("post/{id}")
    suspend fun deletePost(@Path("id") idPost: Int): Response<Int>

    @Multipart
    @POST("photo/upload")
    suspend fun uploadPhoto(@Part photo: MultipartBody.Part): Response<Map<String, String>>
}