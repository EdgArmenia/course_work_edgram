package com.example.coursework.view.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.coursework.R
import com.example.coursework.appComponent
import com.example.coursework.databinding.FragmentSignInBinding
import com.example.coursework.utils.Constants
import com.example.coursework.utils.error.EntityException
import com.example.coursework.utils.error.NetworkException
import com.example.coursework.utils.error.ServerException
import com.example.coursework.utils.error.SuccessException
import com.example.coursework.utils.error.ValidateException
import com.example.coursework.utils.toast
import com.example.coursework.viewmodel.SignInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SignInViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var binding: FragmentSignInBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        context.appComponent.injectSignInFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.edTextEmail.setText("edor.merdez@yandex.ru")
        binding.edTextPassword.setText("1111")

        binding.signInBtn.setOnClickListener { signIn() }
        binding.signUpTextBtn.setOnClickListener { navigateToSignUp() }

        return binding.root
    }

    private fun signIn() {
        val email: String = binding.edTextEmail.text.toString()
        val password: String = binding.edTextPassword.text.toString()

        lifecycleScope.launch {
            try {
                if (viewModel.signIn(email, password)) {
                    findNavController().navigate(R.id.action_signInFragment_to_menuFragment)
                } else requireContext().toast(Constants.USER_NOT_EXIST)

            } catch (e: NetworkException) {
                requireContext().toast(Constants.NO_INTERNET_CONNECTION)
            } catch (e: ServerException) {
                requireContext().toast(Constants.SERVER_ERROR)
            } catch (e: ValidateException) {
                showValidError()
            }
        }
    }

    private fun showValidError() = with(binding) {
        edTextEmail.error = Constants.ENTERED_DATA_IS_NOT_VALID
        edTextPassword.error = Constants.ENTERED_DATA_IS_NOT_VALID
    }

    private fun navigateToSignUp() {
        findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
    }

}