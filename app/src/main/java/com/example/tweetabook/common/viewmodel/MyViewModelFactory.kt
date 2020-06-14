package com.example.tweetabook.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tweetabook.common.di.ControllerCompositionRoot
import com.example.tweetabook.screens.main.viewmodel.MainViewModel

open class MyViewModelFactory(private val controllerCompositionRoot: ControllerCompositionRoot): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(controllerCompositionRoot) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}