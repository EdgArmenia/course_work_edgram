package com.example.coursework.view.recyclerviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.databinding.PostItemBinding
import com.example.coursework.model.entity.PostModel
import com.example.coursework.view.contracts.PostListener

class PostsAdapter(
    private val posts: List<PostModel>,
    private val context: Context,
    private val listener: PostListener
) : RecyclerView.Adapter<PostsAdapter.PostsHolder>() {
    class PostsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = PostItemBinding.bind(itemView)

        fun bind(post: PostModel, context: Context, listener: PostListener) = with(binding) {
//            userName.text =
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)

        return PostsHolder(view)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostsHolder, position: Int) {
        holder.bind(posts[position], context, listener)
    }
}