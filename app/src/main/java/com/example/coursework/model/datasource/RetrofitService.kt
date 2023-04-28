package com.example.coursework.model.datasource

import com.example.coursework.model.entity.LikesModel
import com.example.coursework.model.entity.PostModel
import com.example.coursework.model.entity.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitService {
    @GET("user/{id_user}/{email}/{password}")
    @Headers("Content-Type: application/json")
    suspend fun getUser(@Path("id_user") id_user: Int = 0, @Path("email") email: String = "", @Path("password") password: Int = 0): Response<UserModel?>

    @GET("post")
    @Headers("Content-Type: application/json")
    suspend fun getPosts(): Response<List<PostModel>>

    @GET("likes")
    @Headers("Content-Type: application/json")
    suspend fun getUsersWithLike(@Body idPost: Int): Response<List<UserModel>>

    @POST("user/0/0/0")
    suspend fun postUser(@Body user: UserModel): Response<Int>

    @POST("post")
    suspend fun postPost(@Body post: PostModel): Response<Int>

    @POST("likes")
    suspend fun postLike(@Body likes: LikesModel): Response<Int>

    @PUT("user")
    suspend fun putUser(@Body user: UserModel): Response<Int>

    @PUT("post")
    suspend fun putPost(@Body post: PostModel): Response<Int>

    @DELETE("user")
    suspend fun deleteUser(@Body idUser: Int): Response<Int>

    @DELETE("post")
    suspend fun deletePost(@Body idPost: Int): Response<Int>
}