package com.example.tweetabook.common

import android.app.Application
import com.example.tweetabook.common.di.CompositionRoot

public class BaseApplication: Application() {
    lateinit var compositionRoot: CompositionRoot

    override fun onCreate() {
        super.onCreate()
        compositionRoot = CompositionRoot()
    }
}