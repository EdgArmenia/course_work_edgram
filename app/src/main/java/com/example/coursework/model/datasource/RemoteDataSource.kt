package com.example.coursework.model.datasource

import com.example.coursework.model.entity.LikesModel
import com.example.coursework.model.entity.PostModel
import com.example.coursework.model.entity.UserModel
import com.example.coursework.utils.error.ServerException
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getUser(idUser: Int = 0, email: String, password: Int): Response<UserModel?>
    suspend fun getPosts(): Response<List<PostModel>>
    suspend fun getLike(idPost: Int): Response<List<UserModel>>

    suspend fun postUser(user: UserModel): Response<Int>
    suspend fun postPost(post: PostModel): Response<Int>
    suspend fun postLike(like: LikesModel): Response<Int>

    suspend fun putUser(user: UserModel): Response<Int>
    suspend fun putPost(post: PostModel): Response<Int>

    suspend fun deleteUser(idUser: Int): Response<Int>
    suspend fun deletePost(idPost: Int): Response<Int>

}

class RemoteDataSourceImpl(private val retrofitService: RetrofitService) : RemoteDataSource {
    override suspend fun getUser(idUser: Int, email: String, password: Int): Response<UserModel?> {
        try {
            return retrofitService.getUser(idUser, email, password)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun getPosts(): Response<List<PostModel>> {
        try {
            return retrofitService.getPosts()
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun getLike(idPost: Int): Response<List<UserModel>> {
        try {
            return retrofitService.getUsersWithLike(idPost)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun postUser(user: UserModel): Response<Int> {
        try {
            return retrofitService.postUser(user)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun postPost(post: PostModel): Response<Int> {
        try {
            return retrofitService.postPost(post)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun postLike(like: LikesModel): Response<Int> {
        try {
            return retrofitService.postLike(like)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun putUser(user: UserModel): Response<Int> {
        try {
            return retrofitService.putUser(user)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun putPost(post: PostModel): Response<Int> {
        try {
            return retrofitService.putPost(post)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun deleteUser(idUser: Int): Response<Int> {
        try {
            return retrofitService.deleteUser(idUser)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    override suspend fun deletePost(idPost: Int): Response<Int> {
        try {
            return retrofitService.deletePost(idPost)
        } catch (e: ServerException) {
            throw ServerException()
        }
    }
}