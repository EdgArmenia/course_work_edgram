package com.example.coursework.view.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coursework.R
import com.example.coursework.appComponent
import com.example.coursework.databinding.FragmentPostsBinding
import com.example.coursework.model.entity.LikesModel
import com.example.coursework.model.entity.PostModel
import com.example.coursework.utils.Constants
import com.example.coursework.utils.extensions.toast
import com.example.coursework.view.contracts.PostsListener
import com.example.coursework.view.recyclerviews.PostsAdapter
import com.example.coursework.viewmodel.PostsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class PostsFragment : Fragment(), PostsListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PostsViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentPostsBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        context.appComponent.injectPostsFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPosts()

        viewModel.postsAndLikes.observe(viewLifecycleOwner) {
            showPosts(it.posts, it.likes)
        }
    }

    private fun showPosts(posts: List<PostModel>, likes: List<LikesModel>) = with(binding) {
        postsRcView.layoutManager = LinearLayoutManager(requireContext())
        postsRcView.adapter = PostsAdapter(posts, likes, this@PostsFragment)
    }

    override fun onClickShowUsersLiked(post: PostModel) {
        if (post.likes != 0)
            findNavController().navigate(
                R.id.action_postsFragment_to_likesViewFragment,
                bundleOf(Constants.POST_CODE to post)
            )
        else requireContext().toast(Constants.NO_LIKES)
    }

    override fun onClickLike(idPost: Int) {
        val likeButton = requireActivity().findViewById<ImageButton>(R.id.like_button)
        lifecycleScope.launch(Dispatchers.IO) {
            val status = async { withTimeout(1500L) { viewModel.postLike(idPost) } }

            when (status.await()) {
                "liked" -> {
                    requireActivity().runOnUiThread {
                        likeButton.setBackgroundResource(R.drawable.like_filled)
                    }
                }

                "unliked" -> {
                    requireActivity().runOnUiThread {
                        likeButton.setBackgroundResource(R.drawable.like_no_filled)
                    }
                }
            }
        }
    }
}