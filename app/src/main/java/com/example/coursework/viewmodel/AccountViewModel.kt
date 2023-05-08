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
import java.lang.NullPointerException
import java.lang.RuntimeException
import java.net.ProtocolException
import javax.inject.Inject

class AccountViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val postsAndLikes = MutableLiveData<PostAndLikesData>()

    fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val postsResponse = async { repository.getPosts(MyAccount.user.value!!.idUser) }
            val likesResponse = async { repository.getUsersWithLike() }

            if (postsResponse.await().isSuccessful && likesResponse.await().isSuccessful) {
                try {
                    postsAndLikes.postValue(
                        PostAndLikesData(
                            postsResponse.await().body()!!, likesResponse.await().body()!!
                        )
                    )
                } catch (e: NullPointerException) {
                    throw CancellationException("Live data error")
                }
            }
        }
    }

    suspend fun postLike(id_post: Int): String {
        try {
            val response = repository.postLike(LikesModel(MyAccount.user.value?.idUser!!, id_post))

            if (response.isSuccessful) {
                return response.body()!!["status"]!!
            } else throw ServerException()
        } catch (e: ProtocolException) {
            throw ServerException()
        } catch (e: ServerException) {
            throw ServerException()
        }
    }

    suspend fun deletePost(id_post: Int): Boolean {
        try {
            val response = repository.deletePost(id_post)

            if (response.isSuccessful) {
                if (response.body() == 1)
                    return true
                else if(response.body() == -1)
                    return false
            } else throw ServerException()
        } catch (e: ServerException) {
            throw ServerException()
        }
        throw RuntimeException()
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