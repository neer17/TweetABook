package com.example.tweetabook.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tweetabook.R
import com.example.tweetabook.screens.common.views.BaseObservableViewMvc

class MainViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup
) : BaseObservableViewMvc<MainViewMvc.Listener>(), MainViewMvc{

    init {
        setRootView(inflater.inflate(R.layout.fragment_main, parent, false))
    }
}