package com.example.tweetabook.common.di

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import com.example.tweetabook.api.MyBackendApi
import com.example.tweetabook.screens.common.FragmentFrameWrapper
import com.example.tweetabook.screens.common.ViewMvcFactory
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator

class ControllerCompositionRoot(
    private val compositionRoot: CompositionRoot,
    private val activity: FragmentActivity
) {
    fun getViewMvcFactory(screenNavigator: ScreenNavigator): ViewMvcFactory {
        return ViewMvcFactory(getLayoutInflater(), screenNavigator)
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
}