package com.example.tweetabook.common.di

import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import com.example.tweetabook.api.MyBackendApi
import com.example.tweetabook.screens.common.ViewMvcFactory

class ControllerCompositionRoot(
    private val compositionRoot: CompositionRoot,
    private val activity: FragmentActivity
) {
    fun getViewMvcFactory(): ViewMvcFactory {
        return ViewMvcFactory(getLayoutInflater())
    }

    private fun getLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(activity)
    }

    fun getBackendApi(): MyBackendApi {
        return compositionRoot.getBackendApi()
    }
}