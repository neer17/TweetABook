package com.example.tweetabook.screens.auth

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tweetabook.R
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator
import com.example.tweetabook.screens.common.views.BaseObservableViewMvc

class AuthViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup,
    screenNavigator: ScreenNavigator
) :
    BaseObservableViewMvc<AuthViewMvc.Listener>(), AuthViewMvc {

    init {
        setRootView(inflater.inflate(R.layout.fragment_auth, parent, false))
    }

}