package com.example.coursework.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.coursework.R
import com.example.coursework.databinding.FragmentMenuBinding
import com.example.coursework.model.entity.UserModel

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding
    private lateinit var user: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHost: NavHostFragment =
            childFragmentManager.findFragmentById(R.id.menu_fragment_holder) as NavHostFragment

        val navController = navHost.navController

        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

        binding.addPostBtn.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_newPostFragment)
        }
    }

}