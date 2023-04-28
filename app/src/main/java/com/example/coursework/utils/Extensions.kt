package com.example.coursework.utils

import android.content.Context
import android.widget.Toast
import com.example.coursework.MyApplication
import com.example.coursework.appComponent
import com.example.coursework.di.AppComponent

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}