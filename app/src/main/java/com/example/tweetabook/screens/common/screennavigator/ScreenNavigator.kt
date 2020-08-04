package com.example.tweetabook.screens.common.screennavigator

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class ScreenNavigator(
    private val activity: FragmentActivity,
    savedInstanceState: Bundle?,
    private val frameLayoutId: Int
) {
   /* private lateinit var fragNavController: FragNavController

    init {
        fragNavController = getFragNavController(savedInstanceState)
    }

    fun navigateToMainFrag() {
        fragNavController.switchTab(FragNavController.TAB1)
    }

    fun navigateToCameraActivity(fragment: Fragment) {
        CropImage.activity()
            .start(activity, fragment);
    }

    private fun getFragNavController(savedInstanceState: Bundle?): FragNavController {
        val fragNavController = FragNavController(activity.supportFragmentManager, frameLayoutId)
        val fragments = listOf<Fragment>(MainFragment.newInstance())
        fragNavController.rootFragments = fragments
        fragNavController.initialize(FragNavController.TAB1, savedInstanceState)
        return fragNavController
    }

    fun saveInstanceState(savedInstanceState: Bundle) {
        fragNavController.onSaveInstanceState(savedInstanceState)
    }*/
}