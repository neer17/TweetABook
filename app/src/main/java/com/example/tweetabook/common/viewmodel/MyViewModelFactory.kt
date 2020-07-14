package com.example.tweetabook.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tweetabook.common.di.SingleActivityController
import com.example.tweetabook.screens.main.viewmodel.MainViewModel

open class MyViewModelFactory(private val singleActivityController: SingleActivityController): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(singleActivityController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}