package com.example.coursework.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.model.entity.LikesModel
import com.example.coursework.model.liveuser.MyAccount
import com.example.coursework.model.repository.Repository
import com.example.coursework.utils.PostAndLikesData
import com.example.coursework.utils.error.ServerException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.lang.NullPointerException
import java.lang.RuntimeException
import java.net.ProtocolException
import javax.inject.Inject

class AccountViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val postsAndLikes = MutableLiveData<PostAndLikesData>()

    fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val postsResponse = async { withTimeout(1500L) { repository.getPosts(MyAccount.user.value!!.idUser) } }
            val likesResponse = async { withTimeout(1500L) { repository.getLikes(MyAccount.user.value!!.idUser) } }

            if (postsResponse.await().isSuccessful && likesResponse.await().isSuccessful) {
                try {
                    postsAndLikes.postValue(
                        PostAndLikesData(
                            postsResponse.await().body()!!.toMutableList(),
                            likesResponse.await().body()!!.toMutableList()
                        )
                    )
                } catch (e: NullPointerException) {
                    throw CancellationException("Live data error")
                } catch (e: ProtocolException) {
                    throw ServerException()
                }
            }
        }
    }

    suspend fun postLike(idPost: Int): String {
        try {
            val response = withTimeout(1500L) {
                repository.postLike(
                    LikesModel(
                        MyAccount.user.value?.idUser!!,
                        idPost
                    )
                )
            }

            if (response.isSuccessful) {
                return response.body()!!["status"]!!
            } else throw ServerException()
        } catch (e: ProtocolException) {
            throw ServerException()
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    suspend fun deletePost(idPost: Int): Boolean {
        try {
            val response = withTimeout(1500L) { repository.deletePost(idPost) }

            if (response.isSuccessful) {
                if (response.body() == 1) {
                    deletePostInLiveData(idPost)
                    return true
                } else if (response.body() == -1)
                    return false
            } else throw ServerException()
        } catch (e: ServerException) {
            throw ServerException()
        } catch (e: ProtocolException) {
            throw ServerException()
        }
        throw RuntimeException()
    }

    private fun deletePostInLiveData(idPost: Int) {
        val index = findIndex(idPost)
        postsAndLikes.value?.posts?.removeAt(index)
    }

    private fun findIndex(idPost: Int): Int {
        for (post in postsAndLikes.value?.posts!!) {
            if (post.idPost == idPost)
                return postsAndLikes.value?.posts!!.indexOf(post)
        }
        return 0
    }

    fun countLikes(): Int {
        var likes: Int = 0
        viewModelScope.launch {
            for (post in postsAndLikes.value?.posts!!)
                likes += post.likes
        }

        return likes
    }
}