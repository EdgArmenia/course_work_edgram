package com.example.coursework.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coursework.databinding.FragmentPostsBinding
import com.example.coursework.view.contracts.UsersPostsListener

class PostsFragment : Fragment(), UsersPostsListener {
    private lateinit var binding: FragmentPostsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onClickShowUsersLiked() {
        TODO("Not yet implemented")
    }

    override fun onClickLike() {
        TODO("Not yet implemented")
    }
}