package com.example.tweetabook.screens.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tweetabook.common.BaseApplication
import com.example.tweetabook.common.di.ControllerCompositionRoot

open class BaseActivity: AppCompatActivity() {
    public lateinit var controllerCompositionRoot: ControllerCompositionRoot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val compositionRoot = (application as BaseApplication).compositionRoot
        controllerCompositionRoot = ControllerCompositionRoot(compositionRoot, this)
    }
}