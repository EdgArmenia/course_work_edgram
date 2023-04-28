package com.example.coursework.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class SignUpFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SignUpViewModel

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        context.appComponent.injectSignUpFragment(this)

        viewModel = ViewModelProviders.of(
            this@SignUpFragment,
            viewModelFactory
        )[SignUpViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.btnAddPhoto.setOnClickListener { addPhoto() }
        binding.btnRegister.setOnClickListener { signUp() }

        binding.imageView.setImageResource(R.drawable.baseline_account_circle_24)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult -> try {
                binding.imageView.setImageURI(result.data?.data)
            } catch (e: Exception) {
                requireContext().toast("Operation failed....")
            }

        }
        return binding.root
    }

    private fun addPhoto() {
        val intentGallery: Intent = Intent(Intent.ACTION_PICK)

        intentGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        launcher.launch(intentGallery)
    }

    private fun signUp() {
        val name: String = binding.edTextName.text.toString()
        val email: String = binding.edTextEmail.text.toString()
        val password: String = binding.edTextPassword.text.toString()

        lifecycleScope.launch {
            try {
                if (viewModel.signUp(name, email, password)) {
                    requireContext().toast("Successfully")
                    findNavController().popBackStack()
                }
                else requireContext().toast("Operation failed...")
            } catch (e: ServerException) {
                requireContext().toast(Constants.SERVER_ERROR)
            } catch (e: ValidateException) {
                showValidError()
            }
        }
    }

    private fun showValidError() = with(binding) {
        edTextName.error = Constants.ENTERED_DATA_IS_NOT_VALID
        edTextEmail.error = Constants.ENTERED_DATA_IS_NOT_VALID
        edTextPassword.error = Constants.ENTERED_DATA_IS_NOT_VALID
    }
}