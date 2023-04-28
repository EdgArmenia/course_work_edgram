package com.example.coursework.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coursework.viewmodel.AccountSettingsViewModel
import com.example.coursework.viewmodel.AccountViewModel
import com.example.coursework.viewmodel.NewPostViewModel
import com.example.coursework.viewmodel.PostsViewModel
import com.example.coursework.viewmodel.SignInViewModel
import com.example.coursework.viewmodel.SignUpViewModel
import com.example.coursework.viewmodel.factory.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("DEPRECATION")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    internal abstract fun bindSignUpViewModel(signUpViewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    internal abstract fun bindSignInViewModel(signInViewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostsViewModel::class)
    internal abstract fun bindPostsViewModel(postsViewModel: PostsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewPostViewModel::class)
    internal abstract fun bindNewPostViewModel(newPostViewModel: NewPostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    internal abstract fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountSettingsViewModel::class)
    internal abstract fun bindAccountSettingsViewModel(accountSettingsViewModel: AccountSettingsViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}