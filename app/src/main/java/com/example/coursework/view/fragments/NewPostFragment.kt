package com.example.coursework.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coursework.R
import com.example.coursework.databinding.FragmentNewPostBinding

class NewPostFragment : Fragment() {
    private lateinit var binding: FragmentNewPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        return binding.root
    }
}