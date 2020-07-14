package com.example.tweetabook.common.di

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import com.example.tweetabook.api.MyBackendApi
import com.example.tweetabook.screens.common.FragmentFrameWrapper
import com.example.tweetabook.screens.common.SingleActivity
import com.example.tweetabook.screens.common.ViewMvcFactory
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator
import com.example.tweetabook.socket.MySocket

class SingleActivityController(
    private val compositionRoot: CompositionRoot,
    private val activity: FragmentActivity
) {
    val mySocket: MySocket by lazy {
        MySocket()
    }

    fun getViewMvcFactory(screenNavigator: ScreenNavigator): ViewMvcFactory {
        return ViewMvcFactory(getLayoutInflater(), screenNavigator, getBackendApi(), this)
    }

    private fun getLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(activity)
    }

     fun getBackendApi(): MyBackendApi {
        return compositionRoot.getBackendApi()
    }

    fun getScreenNavigator(savedInstanceState: Bundle?): ScreenNavigator {
        return ScreenNavigator(activity, savedInstanceState, getFragmentFrameId())
    }

    private fun getFragmentFrameId(): Int {
        return (activity as FragmentFrameWrapper).getFrameLayout().id
    }

    private fun getContext(): Context {
        return activity
    }

    fun getActivity(): SingleActivity {
        return activity as SingleActivity
    }

    fun getNetworkInfo(): Boolean {
        return compositionRoot.registerNetworkCallback(getContext())
    }

    fun getViewLifecycleOwner(): SingleActivity = getActivity()
}