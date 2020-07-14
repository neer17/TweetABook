package com.example.tweetabook.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tweetabook.api.MyBackendApi
import com.example.tweetabook.common.di.SingleActivityController
import com.example.tweetabook.screens.auth.AuthViewMvcImpl
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator
import com.example.tweetabook.screens.main.MainFragment
import com.example.tweetabook.screens.main.MainViewMvcImpl
import com.example.tweetabook.screens.main.viewmodel.MainViewModel

class ViewMvcFactory(
    private val layoutInflater: LayoutInflater,
    private val screenNavigator: ScreenNavigator,
    private val myBackendApi: MyBackendApi,
    private val singleActivityController: SingleActivityController
) {
    fun getMainViewMvcImpl(parent: ViewGroup, viewModel: MainViewModel, fragment: MainFragment): MainViewMvcImpl {
        return MainViewMvcImpl(layoutInflater, parent, screenNavigator, viewModel, singleActivityController, fragment)
    }

    fun getAuthViewMvcImpl(parent: ViewGroup): AuthViewMvcImpl {
        return AuthViewMvcImpl(layoutInflater, parent, screenNavigator)
    }
}