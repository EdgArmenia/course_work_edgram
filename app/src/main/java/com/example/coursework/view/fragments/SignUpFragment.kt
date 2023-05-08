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
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.coursework.R
import com.example.coursework.appComponent
import com.example.coursework.databinding.FragmentSignUpBinding
import com.example.coursework.utils.Constants
import com.example.coursework.utils.error.ServerException
import com.example.coursework.utils.error.ValidateException
import com.example.coursework.utils.toast
import com.example.coursework.viewmodel.SignUpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ProtocolException
import javax.inject.Inject
import kotlin.Exception

class SignUpFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SignUpViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var binding: FragmentSignUpBinding
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

        context.appComponent.injectSignUpFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.apply {
            backBtn.setBackgroundResource(R.drawable.baseline_arrow_back_ios_24)
            backBtn.setOnClickListener { findNavController().popBackStack() }
            btnAddPhoto.setOnClickListener { launcher.launch(arrayOf("image/*")) }
            btnRegister.setOnClickListener { signUp() }

            imageView.setImageResource(R.drawable.baseline_account_circle_24)
        }
        return binding.root
    }

    private fun signUp() {
        val name: String = binding.edTextName.text.toString()
        val email: String = binding.edTextEmail.text.toString()
        val password: String = binding.edTextPassword.text.toString()

        val image = try {
            viewModel.openFile(requireActivity().contentResolver)
        } catch (e: java.lang.Exception) {
            showValidError()
            return
        }


        lifecycleScope.launch(Dispatchers.IO) {
            try {
                if (viewModel.signUp(name, email, password, image)) {
                    requireContext().toast("Successfully")
                    findNavController().popBackStack()
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
                requireActivity().runOnUiThread {
                    showValidError()
                }
            }
        }
    }

    private fun showValidError() = with(binding) {
        requireContext().toast("There is no image selected...")
        edTextName.error = Constants.ENTERED_DATA_IS_NOT_VALID
        edTextEmail.error = Constants.ENTERED_DATA_IS_NOT_VALID
        edTextPassword.error = Constants.ENTERED_DATA_IS_NOT_VALID
    }
}