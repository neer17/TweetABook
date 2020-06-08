package com.example.tweetabook.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tweetabook.commons.BaseApplication
import com.example.tweetabook.commons.di.ControllerCompositionRoot

open class BaseActivity: AppCompatActivity() {
    lateinit var controllerCompositionRoot: ControllerCompositionRoot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val compositionRoot = (application as BaseApplication).compositionRoot
        controllerCompositionRoot = ControllerCompositionRoot(compositionRoot, this)
    }
}