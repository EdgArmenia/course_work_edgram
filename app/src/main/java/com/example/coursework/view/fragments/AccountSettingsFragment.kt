package com.example.coursework.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.coursework.R
import com.example.coursework.appComponent
import com.example.coursework.databinding.FragmentAccountSettingsBinding
import com.example.coursework.viewmodel.AccountSettingsViewModel
import com.example.coursework.viewmodel.factory.ViewModelFactory
import javax.inject.Inject

class AccountSettingsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AccountSettingsViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentAccountSettingsBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.injectAccountSettingsFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountSettingsBinding.inflate(inflater, container, false)

        binding.apply {
            backBtn.setOnClickListener { findNavController().popBackStack() }
        }

        return binding.root
    }
}