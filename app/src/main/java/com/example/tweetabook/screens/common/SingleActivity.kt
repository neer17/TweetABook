package com.example.tweetabook.screens.common

import android.os.Bundle
import android.widget.FrameLayout
import com.example.tweetabook.R
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator

class SingleActivity : BaseActivity(), FragmentFrameWrapper {
    private val TAG = "AppDebug: " + SingleActivity::class.java.simpleName

    private lateinit var screenNavigator: ScreenNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        screenNavigator = controllerCompositionRoot.getScreenNavigator(savedInstanceState)

        screenNavigator.navigateToMainFrag()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        screenNavigator.saveInstanceState(outState)
    }

    override fun getFrameLayout(): FrameLayout {
        return findViewById(R.id.frame_content) as FrameLayout
    }
}