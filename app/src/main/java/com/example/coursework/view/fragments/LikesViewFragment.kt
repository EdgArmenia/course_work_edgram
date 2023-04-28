package com.example.coursework.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coursework.R
import com.example.coursework.databinding.FragmentLikesViewBinding

class LikesViewFragment : Fragment() {
    private lateinit var binding: FragmentLikesViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikesViewBinding.inflate(inflater, container, false)

        return binding.root
    }
}