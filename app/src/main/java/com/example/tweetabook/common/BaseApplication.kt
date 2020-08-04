package com.example.tweetabook.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
public class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}