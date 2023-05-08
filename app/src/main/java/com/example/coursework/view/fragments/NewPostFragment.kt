package com.example.coursework.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.coursework.R
import com.example.coursework.appComponent
import com.example.coursework.databinding.FragmentNewPostBinding
import com.example.coursework.model.entity.PostModel
import com.example.coursework.utils.Constants
import com.example.coursework.utils.error.ServerException
import com.example.coursework.utils.toast
import com.example.coursework.viewmodel.NewPostViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewPostFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: NewPostViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentNewPostBinding

    private val launcher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            try {
                viewModel.imageUri.value = uri
                binding.imageView.setImageURI(uri)
            } catch (e: Exception) {
                requireContext().toast("Can't open image")
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.injectNewPostFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)

        viewModel.post.value = requireArguments().getSerializable(Constants.POST_CODE) as PostModel?
        viewModel.isEdit.value = requireArguments().getBoolean(Constants.TO_EDIT_POST)

        binding.apply {
            backBtn.setBackgroundResource(R.drawable.baseline_arrow_back_ios_24)
            backBtn.setOnClickListener { findNavController().popBackStack() }

            if (viewModel.post.value != null) {
                Glide.with(requireContext()).load(viewModel.post.value!!.photo).into(imageView)
                edTextLabel.setText(viewModel.post.value!!.label)
            }

            title.text = if (viewModel.isEdit.value!!) "Edit Post" else "New Post"
            btnPost.text = if (viewModel.isEdit.value!!) "Edit" else "Post"

            btnAddPhoto.setOnClickListener { launcher.launch(arrayOf("image/*")) }
            btnPost.setOnClickListener { if (viewModel.isEdit.value!!) put() else post() }
        }
        return binding.root
    }

    private fun post() {
        val label: String = binding.edTextLabel.text.toString()

        val image = try {
            viewModel.openFile(requireActivity().contentResolver)
        } catch (e: Exception) {
            showPhotoError()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                if (viewModel.postPost(label, image)) {
                    requireActivity().runOnUiThread {
                        requireContext().toast("Successfully")
                        findNavController().popBackStack()
                    }
                } else {
                    requireActivity().runOnUiThread {
                        requireContext().toast("Operation failed...")
                    }
                }
            } catch (e: ServerException) {
                requireActivity().runOnUiThread {
                    requireContext().toast(Constants.SERVER_ERROR)
                }
            }
        }
    }

    private fun put() {
        val label: String = binding.edTextLabel.text.toString()

        val image = try {
            viewModel.openFile(requireActivity().contentResolver)
        } catch (e: NullPointerException) {
            null
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                if (viewModel.putPost(label, image)) {
                    requireActivity().runOnUiThread {
                        requireContext().toast("Successfully")
                        findNavController().popBackStack()
                    }
                } else {
                    requireActivity().runOnUiThread {
                        requireContext().toast("Operation failed...")
                    }
                }
            } catch (e: ServerException) {
                requireActivity().runOnUiThread {
                    requireContext().toast(Constants.SERVER_ERROR)
                }
            }
        }
    }

    private fun showPhotoError() {
        requireContext().toast("There is no image selected...")
    }
}