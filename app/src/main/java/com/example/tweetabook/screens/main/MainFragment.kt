package com.example.tweetabook.screens.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tweetabook.common.di.SingleActivityController
import com.example.tweetabook.screens.common.BaseFragment
import com.example.tweetabook.screens.common.SingleActivity
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator
import com.theartofdev.edmodo.cropper.CropImage


class MainFragment : BaseFragment(), MainViewMvc.Listener {
    companion object {
        @JvmStatic
        fun newInstance(): MainFragment {
            Log.d("MainFragment", "newInstance: ")
            return MainFragment()
        }
    }

    private lateinit var screenNavigator: ScreenNavigator
    private lateinit var singleActivityController: SingleActivityController
    private lateinit var mainViewMvc: MainViewMvc

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")

        singleActivityController = (activity as SingleActivity).singleActivityController

        requireActivity().let {
            screenNavigator = (it as SingleActivity).screenNavigator
            mainViewMvc = singleActivityController.getViewMvcFactory(screenNavigator)
                .getMainViewMvcImpl(container!!, it.mainViewModel,this)
        }

        return mainViewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        mainViewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        mainViewMvc.unregisterListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        mainViewMvc.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        screenNavigator.saveInstanceState(outState)
    }

    override fun onFabBtnClicked() {
        CropImage.activity()
            .start(requireContext(), this);
    }
}