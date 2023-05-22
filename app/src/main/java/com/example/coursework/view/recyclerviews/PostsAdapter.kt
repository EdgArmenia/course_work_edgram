package com.example.coursework.view.recyclerviews

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coursework.R
import com.example.coursework.databinding.PostItemBinding
import com.example.coursework.model.entity.LikesModel
import com.example.coursework.model.entity.PostModel
import com.example.coursework.model.liveuser.MyAccount
import com.example.coursework.view.contracts.AccountPostListener
import com.example.coursework.view.contracts.PostsListener

class PostsAdapter(
    private val posts: List<PostModel>,
    private val likes: List<LikesModel>,
    private val listener: Any,
    private val editable: Boolean = false
) : RecyclerView.Adapter<PostsAdapter.PostsHolder>() {
    class PostsHolder(
        itemView: View,
        private val listener: Any,
        private val editable: Boolean,
    ) : RecyclerView.ViewHolder(itemView) {
        private val binding = PostItemBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(post: PostModel, likes: List<LikesModel>) =
            with(binding) {
                var liked: Boolean
                userName.text = post.name
                likesCount.text = "Likes: ${post.likes}"
                label.text = post.label
                date.text = post.date
                Glide.with(itemView).load(post.avatarPhoto).into(accountPhoto)
                Glide.with(itemView).load(post.photo).into(postPhoto)

                if (LikesModel(MyAccount.user.value?.idUser!!, post.idPost) in likes) {
                    binding.likeButton.setBackgroundResource(R.drawable.like_filled)
                    liked = true
                } else {
                    binding.likeButton.setBackgroundResource(R.drawable.like_no_filled)
                    liked = false
                }

                when (editable) {
                    true -> {
                        binding.editBtn.visibility = View.VISIBLE
                        binding.deletePost.setBackgroundResource(R.drawable.baseline_delete_outline_24)
                        binding.deletePost.visibility = View.VISIBLE
                    }

                    false -> {
                        binding.editBtn.visibility = View.GONE
                        binding.deletePost.visibility = View.GONE
                    }
                }

                when (listener) {
                    is AccountPostListener -> {
                        editBtn.setOnClickListener {
                            listener.onClickEditPost(post)
                        }
                        deletePost.setOnClickListener {
                            listener.onDeletePost(post)
                        }
                        likeButton.setOnClickListener {
                            listener.onClickLike(post.idPost)
                            if (liked) {
                                likesCount.text = "Likes: ${--post.likes}"
                                liked = !liked
                            } else {
                                likesCount.text = "Likes: ${++post.likes}"
                                liked = !liked
                            }
                        }
                        likesCount.setOnClickListener {
                            listener.onClickShowUsersLiked(post)
                        }
                    }

                    is PostsListener -> {
                        likesCount.setOnClickListener {
                            listener.onClickShowUsersLiked(post)
                        }
                        likeButton.setOnClickListener {
                            listener.onClickLike(post.idPost)
                            if (liked) {
                                likesCount.text = "Likes: ${--post.likes}"
                                liked = !liked
                            } else {
                                likesCount.text = "Likes: ${++post.likes}"
                                liked = !liked
                            }
                        }
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)

        return PostsHolder(view, listener, editable)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostsHolder, position: Int) {
        holder.bind(posts[position], likes)
    }
}