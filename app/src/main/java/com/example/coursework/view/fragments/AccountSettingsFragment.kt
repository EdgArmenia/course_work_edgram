package com.example.coursework.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.coursework.R
import com.example.coursework.appComponent
import com.example.coursework.databinding.FragmentAccountSettingsBinding
import com.example.coursework.model.liveuser.MyAccount
import com.example.coursework.utils.Constants
import com.example.coursework.utils.error.ServerException
import com.example.coursework.utils.error.ValidateException
import com.example.coursework.utils.extensions.findTopNavController
import com.example.coursework.utils.extensions.toast
import com.example.coursework.viewmodel.AccountSettingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountSettingsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AccountSettingsViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentAccountSettingsBinding

    private val launcher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            try {
                viewModel.imageUri.postValue(uri)
                binding.imageView.setImageURI(uri)
            } catch (e: Exception) {
                requireContext().toast("Can't open image")
            }
        }

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
            backBtn.setBackgroundResource(R.drawable.baseline_arrow_back_ios_24)
            backBtn.setOnClickListener { findNavController().popBackStack() }
            btnEditAccount.setOnClickListener { putUser() }
            btnAddPhoto.setOnClickListener { launcher.launch(arrayOf("image/*")) }
            deleteAccount.setOnClickListener { deleteAccount() }
            edTextName.setText(MyAccount.user.value!!.name)
            edTextEmail.setText(MyAccount.user.value!!.email)
            edTextPassword.setText(MyAccount.user.value!!.password.toString())
            Glide.with(requireContext()).load(MyAccount.user.value!!.avatarPhoto).into(imageView)
        }

        return binding.root
    }

    private fun putUser() {
        val name: String = binding.edTextName.text.toString()
        val email: String = binding.edTextEmail.text.toString()
        val password: String = binding.edTextPassword.text.toString()

        val image = try {
            viewModel.openFile(requireActivity().contentResolver)
        } catch (e: Exception) {
            null
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                if (viewModel.putUser(name, email, password, image)) {
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
            } catch (e: ValidateException) {
                requireActivity().runOnUiThread { showValidError() }
            }
        }
    }

    private fun deleteAccount() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (viewModel.deleteUser()) {
                requireActivity().runOnUiThread {
                    requireContext().toast("Successfully")
                    findTopNavController().navigate(R.id.action_menuFragment_to_signInFragment)
                }
            }
        }
    }

    private fun showValidError() = with(binding) {
        edTextName.error = Constants.ENTERED_DATA_IS_NOT_VALID
        edTextEmail.error = Constants.ENTERED_DATA_IS_NOT_VALID
        edTextPassword.error = Constants.ENTERED_DATA_IS_NOT_VALID
    }
}