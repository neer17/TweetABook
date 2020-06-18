package com.example.tweetabook.screens.common

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.viewModels
import com.example.tweetabook.R
import com.example.tweetabook.common.viewmodel.MyViewModelFactory
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator
import com.example.tweetabook.screens.main.viewmodel.MainViewModel

class SingleActivity : BaseActivity(), FragmentFrameWrapper {
    private val TAG = "AppDebug: " + SingleActivity::class.java.simpleName

    lateinit var screenNavigator: ScreenNavigator
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        instantiateViewModel()

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

    private fun instantiateViewModel() {
        val mainViewModel: MainViewModel by viewModels { MyViewModelFactory(controllerCompositionRoot) }
        this.mainViewModel = mainViewModel
    }
}