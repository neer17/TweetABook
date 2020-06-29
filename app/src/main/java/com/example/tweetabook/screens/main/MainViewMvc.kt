package com.example.tweetabook.screens.main

import android.content.Intent
import com.example.tweetabook.screens.common.views.ObservableViewMvc

interface MainViewMvc: ObservableViewMvc<MainViewMvc.Listener>{

    interface Listener {
        fun onFabBtnClicked()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    fun onFragmentStop()
    fun onFragmentStart()
}