package com.example.tweetabook.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tweetabook.screens.auth.AuthViewMvcImpl
import com.example.tweetabook.screens.main.MainViewMvcImpl

class ViewMvcFactory(private val mLayoutInflater: LayoutInflater) {
    fun getMainViewMvcImpl(parent: ViewGroup): MainViewMvcImpl {
        return MainViewMvcImpl(mLayoutInflater, parent)
    }

    fun getAuthViewMvcImpl(parent: ViewGroup): AuthViewMvcImpl {
        return AuthViewMvcImpl(mLayoutInflater, parent)
    }
}