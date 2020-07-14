package com.example.tweetabook.screens.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tweetabook.common.BaseApplication
import com.example.tweetabook.common.di.SingleActivityController

open class BaseActivity: AppCompatActivity(), UIChangeListener {
    lateinit var singleActivityController: SingleActivityController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val compositionRoot = (application as BaseApplication).compositionRoot
        singleActivityController = SingleActivityController(compositionRoot, this)
    }

    override fun showProgressBar() {
        showProgressBar(true)
    }

    override fun hideProgressBar() {
       showProgressBar(false)
    }
}