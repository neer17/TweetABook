package com.example.tweetabook.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tweetabook.screens.auth.AuthViewMvcImpl
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator
import com.example.tweetabook.screens.main.MainViewMvcImpl

class ViewMvcFactory(
    private val layoutInflater: LayoutInflater,
    private val screenNavigator: ScreenNavigator
) {
    fun getMainViewMvcImpl(parent: ViewGroup): MainViewMvcImpl {
        return MainViewMvcImpl(layoutInflater, parent, screenNavigator)
    }

    fun getAuthViewMvcImpl(parent: ViewGroup): AuthViewMvcImpl {
        return AuthViewMvcImpl(layoutInflater, parent, screenNavigator)
    }
}