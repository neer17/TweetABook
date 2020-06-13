package com.example.tweetabook.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.example.tweetabook.screens.auth.AuthViewMvcImpl
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator
import com.example.tweetabook.screens.main.MainViewMvcImpl
import com.example.tweetabook.screens.main.viewmodel.MainViewModel

class ViewMvcFactory(
    private val layoutInflater: LayoutInflater,
    private val screenNavigator: ScreenNavigator
) {
    fun getMainViewMvcImpl(parent: ViewGroup, viewModel: MainViewModel, lifecycleOwner: LifecycleOwner): MainViewMvcImpl {
        return MainViewMvcImpl(layoutInflater, parent, screenNavigator, viewModel, lifecycleOwner)
    }

    fun getAuthViewMvcImpl(parent: ViewGroup): AuthViewMvcImpl {
        return AuthViewMvcImpl(layoutInflater, parent, screenNavigator)
    }
}