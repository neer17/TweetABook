package com.example.tweetabook.screens.main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.tweetabook.R
import com.example.tweetabook.common.di.ControllerCompositionRoot
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator
import com.example.tweetabook.screens.common.showToast
import com.example.tweetabook.screens.common.views.BaseObservableViewMvc
import com.example.tweetabook.screens.main.viewmodel.MainViewModel
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_main.view.*


class MainViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup,
    val screenNavigator: ScreenNavigator,
    private val viewModel: MainViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val controllerCompositionRoot: ControllerCompositionRoot,
    private val fragment: MainFragment
) : BaseObservableViewMvc<MainViewMvc.Listener>(), MainViewMvc {
    private val TAG = "AppDebug: MainViewMvcImpl"

    init {
        Log.d(TAG, "init block")
        setRootView(inflater.inflate(R.layout.fragment_main, parent, false))

        fabBtnClicked()
        subscribers()
    }

    private fun subscribers() {
        viewModel.downloadUri.observe(lifecycleOwner, Observer {
            //   send the url to the server
            viewModel.sendDownloadUrlToServer(it)
        })

        viewModel.serverResponse.observe(lifecycleOwner, Observer {
            Log.d(TAG, "observeViewModel: result string: $it")
            controllerCompositionRoot.getActivity()
                .showToast("Image uploaded and sent to the server")
        })
    }

    private fun fabBtnClicked() {
        getRootView().fab.setOnClickListener {
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
//                Log.d(TAG, "onActivityResult: resultUri $resultUri")
                viewModel.setFileUri(resultUri)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.e(TAG, "onActivityResult: result.error", error)
            }
        }
    }

    override fun onFragmentStart() {
        viewModel.socketIOConnection()
    }

    override fun onFragmentStop() {
        viewModel.socketIODisconnection()
    }
}