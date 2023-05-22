package com.example.coursework.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coursework.R
import com.example.coursework.appComponent
import com.example.coursework.databinding.FragmentLikesViewBinding
import com.example.coursework.model.entity.PostModel
import com.example.coursework.model.entity.UserModel
import com.example.coursework.utils.Constants
import com.example.coursework.view.recyclerviews.UsersLikedAdapter
import com.example.coursework.viewmodel.LikesViewViewModel
import javax.inject.Inject

class LikesViewFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LikesViewViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentLikesViewBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        context.appComponent.injectLikesViewFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikesViewBinding.inflate(inflater, container, false)

        binding.apply {
            backBtn.setBackgroundResource(R.drawable.baseline_arrow_back_ios_24)
            backBtn.setOnClickListener { findNavController().popBackStack() }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val post = requireArguments().getSerializable(Constants.POST_CODE) as PostModel
        viewModel.getUsersLiked(post)

        viewModel.usersLiked.observe(viewLifecycleOwner) {
            showLikes(it)
        }
    }

    private fun showLikes(users: List<UserModel>) = with(binding) {
        usersLikedRcView.layoutManager = LinearLayoutManager(requireContext())
        usersLikedRcView.adapter = UsersLikedAdapter(users)
    }
}