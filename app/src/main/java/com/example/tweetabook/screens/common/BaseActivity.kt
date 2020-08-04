package com.example.tweetabook.screens.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val compositionRoot = (application as BaseApplication).compositionRoot
//        singleActivityController = SingleActivityController(compositionRoot, this)
    }
}