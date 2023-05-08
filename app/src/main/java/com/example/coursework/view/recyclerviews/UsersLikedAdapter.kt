package com.example.coursework.view.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coursework.R
import com.example.coursework.databinding.UserLikedItemBinding
import com.example.coursework.model.entity.UserModel

class UsersLikedAdapter(private val users: List<UserModel>) :
    RecyclerView.Adapter<UsersLikedAdapter.UsersLikedHolder>() {
    class UsersLikedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserLikedItemBinding.bind(itemView)
        fun bind(user: UserModel) = with(binding) {
            userName.text = user.name
            Glide.with(itemView).load(user.avatarPhoto).into(accountPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersLikedHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_liked_item, parent, false)

        return UsersLikedHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UsersLikedHolder, position: Int) {
        holder.bind(users[position])
    }
}