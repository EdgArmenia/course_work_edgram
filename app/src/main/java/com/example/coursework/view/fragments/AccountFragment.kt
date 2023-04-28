package com.example.coursework.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coursework.databinding.FragmentAccountBinding
import com.example.coursework.view.contracts.AccountPostsListener

class AccountFragment : Fragment(), AccountPostsListener {
    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onClickEditPost() {
        TODO("Not yet implemented")
    }

    override fun onClickShowUsersLiked() {
        TODO("Not yet implemented")
    }

    override fun onClickLike() {
        TODO("Not yet implemented")
    }
}