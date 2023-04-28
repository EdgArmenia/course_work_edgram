package com.example.coursework

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.coursework.di.AppComponent
import com.example.coursework.di.DaggerAppComponent
import com.example.coursework.model.entity.UserModel

class MyApplication : Application() {
    lateinit var appComponent: AppComponent
//    val user = MutableLiveData<UserModel>()

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder().context(this).build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MyApplication -> appComponent
        else -> this.applicationContext.appComponent
    }