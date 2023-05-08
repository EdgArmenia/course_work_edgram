package com.example.coursework.view.fragments

import android.content.Context
import android.os.Bundle
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
import com.bumptech.glide.Glide
import com.example.coursework.R
import com.example.coursework.appComponent
import com.example.coursework.databinding.FragmentAccountBinding
import com.example.coursework.model.entity.LikesModel
import com.example.coursework.model.entity.PostModel
import com.example.coursework.model.liveuser.MyAccount
import com.example.coursework.utils.Constants
import com.example.coursework.utils.PostAndLikesData
import com.example.coursework.utils.toast
import com.example.coursework.view.contracts.AccountPostListener
import com.example.coursework.view.recyclerviews.PostsAdapter
import com.example.coursework.viewmodel.AccountViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountFragment : Fragment(), AccountPostListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AccountViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentAccountBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.injectAccountFragment(this)
        viewModel.getPosts()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        binding.apply {
            exitButton.setOnClickListener { exitFromAccount() }
            editAccountBtn.setOnClickListener { openAccountSetting() }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.postsAndLikes.observe(viewLifecycleOwner) {
            showAccountInfo(it)
            showPosts(it.posts, it.likes)
        }
    }

    private fun showAccountInfo(postsAndLikes: PostAndLikesData) = with(binding) {
        likesCount.text = viewModel.countLikes().toString()
        exitButton.setBackgroundResource(R.drawable.baseline_exit_to_app_24)
        postsCount.text = postsAndLikes.posts.size.toString()
        accountName.text = MyAccount.user.value?.name
        Glide.with(requireContext()).load(MyAccount.user.value?.avatarPhoto).into(accountPhoto)
    }

    private fun showPosts(posts: List<PostModel>, likes: List<LikesModel>) = with(binding) {
        postsRcView.layoutManager = LinearLayoutManager(requireContext())
        postsRcView.adapter = PostsAdapter(posts, likes, this@AccountFragment, true)
    }

    override fun onDeletePost(post: PostModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (viewModel.deletePost(post.idPost)) {
                requireActivity().runOnUiThread {
                    requireContext().toast("Post deleted")
                }
            } else {
                requireActivity().runOnUiThread {
                    requireContext().toast("Can't delete...")
                }
            }
        }
    }

    override fun onClickEditPost(post: PostModel) {
        findNavController().navigate(
            R.id.action_accountFragment_to_newPostFragment2,
            bundleOf(Constants.POST_CODE to post, Constants.TO_EDIT_POST to true)
        )
    }

    override fun onClickShowUsersLiked(post: PostModel) {
        if (post.likes != 0)
            findNavController().navigate(
                R.id.action_accountFragment_to_likesViewFragment2,
                bundleOf(Constants.POST_CODE to post)
            )
        else requireContext().toast(Constants.NO_LIKES)
    }

    override fun onClickLike(idPost: Int) {
        val likeButton = requireActivity().findViewById<ImageButton>(R.id.like_button)
        lifecycleScope.launch(Dispatchers.IO) {
            val status = async { viewModel.postLike(idPost) }

            when (status.await()) {
                "liked" -> setLike(likeButton, R.drawable.like_filled)
                "unliked" -> setLike(likeButton, R.drawable.like_no_filled)
            }
        }
    }

    private fun exitFromAccount() {
        findNavController().navigate(R.id.action_accountFragment_to_signInFragment2)
    }

    private fun openAccountSetting() {
        findNavController().navigate(R.id.action_accountFragment_to_accountSettingsFragment)
    }

    private fun setLike(likeButton: ImageButton, icon: Int) {
        requireActivity().runOnUiThread {
            likeButton.setBackgroundResource(icon)
        }
    }
}