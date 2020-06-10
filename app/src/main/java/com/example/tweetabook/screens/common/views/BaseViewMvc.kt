package com.example.tweetabook.screens.common.views

import android.content.Context
import android.view.View

abstract class BaseViewMvc :
    ViewMvc {
   private lateinit var mRootView: View

    override fun getRootView(): View {
        return mRootView
    }

    protected fun setRootView(rootView: View) {
        this.mRootView = rootView
    }

    protected fun <T : View> findViewById(id: Int): T {
        return getRootView().findViewById(id)
    }

    protected val context: Context
        get() = getRootView().context

}