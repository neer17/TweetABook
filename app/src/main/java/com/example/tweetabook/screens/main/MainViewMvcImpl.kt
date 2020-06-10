package com.example.tweetabook.screens.main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tweetabook.R
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator
import com.example.tweetabook.screens.common.views.BaseObservableViewMvc
import com.theartofdev.edmodo.cropper.CropImage

class MainViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup,
    val screenNavigator: ScreenNavigator
) : BaseObservableViewMvc<MainViewMvc.Listener>(), MainViewMvc{
    private val TAG = "AppDebug: MainViewMvcImpl"

    init {
        Log.d(TAG, "init block")
        setRootView(inflater.inflate(R.layout.fragment_main, parent, false))

        fabBtnClicked()
    }

    private fun fabBtnClicked() {
        findViewById<View>(R.id.fab).setOnClickListener {
           listeners.forEach {
               it.onFabBtnClicked()
           }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri = result.uri
                Log.d(TAG, "onActivityResult: resultUri $resultUri")
                //  TODO: upload to firebase storage
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.e(TAG, "onActivityResult: result.error", error)
            }
        }
    }
}