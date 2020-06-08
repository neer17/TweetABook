package com.example.tweetabook.commons

import android.app.Application
import com.example.tweetabook.commons.di.CompositionRoot

public class BaseApplication: Application() {
    lateinit var compositionRoot: CompositionRoot

    override fun onCreate() {
        super.onCreate()
        compositionRoot = CompositionRoot()
    }
}