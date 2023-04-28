package com.example.coursework.di

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import dagger.MapKey
//import java.lang.annotation.ElementType
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@SuppressLint("SupportAnnotationUsage")
@Deprecated("DEPRECATED_JAVA_ANNOTATION")
@MustBeDocumented
//@Target(ElementType.METHOD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
