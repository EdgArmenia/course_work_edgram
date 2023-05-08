package com.example.coursework.viewmodel

import androidx.lifecycle.ViewModel
import com.example.coursework.model.repository.Repository
import javax.inject.Inject

class AccountSettingsViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

}