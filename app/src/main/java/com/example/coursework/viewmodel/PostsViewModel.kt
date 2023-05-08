package com.example.coursework.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework.model.entity.LikesModel
import com.example.coursework.model.liveuser.MyAccount
import com.example.coursework.model.repository.Repository
import com.example.coursework.utils.PostAndLikesData
import com.example.coursework.utils.error.ServerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import java.net.ProtocolException
import javax.inject.Inject

class PostsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val postsAndLikes = MutableLiveData<PostAndLikesData>()

    fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val postsResponse = async { repository.getPosts() }
            val likesResponse = async { repository.getUsersWithLike() }

            if (postsResponse.await().isSuccessful && likesResponse.await().isSuccessful) {
                try {
                    postsAndLikes.postValue(
                        PostAndLikesData(
                            postsResponse.await().body()!!, likesResponse.await().body()!!
                        )
                    )
                } catch (e: NullPointerException) {
                    // TODO:
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

}