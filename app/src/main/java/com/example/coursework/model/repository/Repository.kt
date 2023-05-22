package com.example.coursework.model.repository

import com.example.coursework.model.datasource.RemoteDataSource
import com.example.coursework.model.entity.LikesModel
import com.example.coursework.model.entity.PostModel
import com.example.coursework.model.entity.UserModel
import com.example.coursework.utils.error.ServerException
import okhttp3.MultipartBody
import retrofit2.Response

interface Repository {
    suspend fun getUser(idUser: Int = 0, email: String, password: Int): Response<UserModel?>
    suspend fun getPosts(idUser: Int = 0): Response<List<PostModel>>
    suspend fun getUsersWithLike(idPost: Int): Response<List<UserModel>>
    suspend fun getLikes(idUser: Int = 0): Response<List<LikesModel>>


    suspend fun postUser(user: UserModel): Response<Int>
    suspend fun postPost(post: PostModel): Response<Int>
    suspend fun postLike(like: LikesModel): Response<Map<String, String>>

    suspend fun putUser(user: UserModel): Response<Int>
    suspend fun putPost(post: PostModel): Response<Int>

    suspend fun deleteUser(idUser: Int): Response<Int>
    suspend fun deletePost(idPost: Int): Response<Int>

    suspend fun uploadPhoto(photo: MultipartBody.Part): Response<Map<String, String>>
}

class RepositoryImpl(private val remoteDataSource: RemoteDataSource) : Repository {
    override suspend fun getUser(idUser: Int, email: String, password: Int): Response<UserModel?> {
        try {
            return remoteDataSource.getUser(idUser, email, password)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun getPosts(idUser: Int): Response<List<PostModel>> {
        try {
            return remoteDataSource.getPosts(idUser)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun getUsersWithLike(idPost: Int): Response<List<UserModel>> {
        try {
            return remoteDataSource.getUsersWithLike(idPost)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun getLikes(idUser: Int): Response<List<LikesModel>> {
        try {
            return remoteDataSource.getLikes(idUser)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun postUser(user: UserModel): Response<Int> {
        try {
            return remoteDataSource.postUser(user)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun postPost(post: PostModel): Response<Int> {
        try {
            return remoteDataSource.postPost(post)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun postLike(like: LikesModel): Response<Map<String, String>> {
        try {
            return remoteDataSource.postLike(like)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun putUser(user: UserModel): Response<Int> {
        try {
            return remoteDataSource.putUser(user)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun putPost(post: PostModel): Response<Int> {
        try {
            return remoteDataSource.putPost(post)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun deleteUser(idUser: Int): Response<Int> {
        try {
            return remoteDataSource.deleteUser(idUser)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun deletePost(idPost: Int): Response<Int> {
        try {
            return remoteDataSource.deletePost(idPost)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun uploadPhoto(photo: MultipartBody.Part): Response<Map<String, String>> {
        try {
            return remoteDataSource.uploadPhoto(photo)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }
}