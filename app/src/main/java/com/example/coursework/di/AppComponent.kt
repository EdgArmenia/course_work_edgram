package com.example.coursework.di

import android.content.Context
import com.example.coursework.view.fragments.AccountFragment
import com.example.coursework.view.fragments.AccountSettingsFragment
import com.example.coursework.view.fragments.NewPostFragment
import com.example.coursework.view.fragments.PostsFragment
import com.example.coursework.view.fragments.SignInFragment
import com.example.coursework.view.fragments.SignUpFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [RemoteDataSourceModule::class,
    RepositoryModule::class, NetworkModule::class,
    ViewModelModule::class])
@Singleton
interface AppComponent {
    fun injectSignInFragment(fragment: SignInFragment)
    fun injectSignUpFragment(fragment: SignUpFragment)
    fun injectPostsFragment(fragment: PostsFragment)
    fun injectNewPostFragment(fragment: NewPostFragment)
    fun injectAccountFragment(fragment: AccountFragment)
    fun injectAccountSettingsFragment(fragment: AccountSettingsFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}