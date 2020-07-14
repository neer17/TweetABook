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
import com.example.tweetabook.common.di.SingleActivityController
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator
import com.example.tweetabook.screens.common.views.BaseObservableViewMvc
import com.example.tweetabook.screens.main.responses.TweetResponse
import com.example.tweetabook.screens.main.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.util.*

class MainViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup,
    val screenNavigator: ScreenNavigator,
    private val viewModel: MainViewModel,
    private val singleActivityController: SingleActivityController,
    private val fragment: MainFragment
) : BaseObservableViewMvc<MainViewMvc.Listener>(), MainViewMvc, MyAdapter.AdapterOnClickListener {
    private val TAG = "AppDebug: MainViewMvcImpl"

    private lateinit var adapter: MyAdapter

    init {
        setRootView(inflater.inflate(R.layout.fragment_main, parent, false))

        recyclerView()
        fabBtnClicked()
        subscribers()
    }

    private fun subscribers() {
        viewModel.serverResponse.observe(getViewLifecycleOwner(), Observer {
            Log.d(TAG, "subscribers: serverResponse: $it")

            //  changing progress in adapter
            val (id, progress, status, tweet) = it
            adapter.updateProgress(id, progress, status, tweet)
        })
    }

    private fun recyclerView() {
        adapter = MyAdapter(this)
        val recyclerView = getRootView().recycler_view
        recyclerView.adapter = adapter
    }


    //  reading image from local storage
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri = result.uri

                val id = UUID.randomUUID().toString()
                val tweetResponse = TweetResponse(id, resultUri)
                adapter.addData(tweetResponse)

                //  upload image and set adapter
                viewModel.uploadImageAndSendDownloadUriToServer(id, resultUri)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.e(TAG, "onActivityResult: result.error", error)
            }
        }
    }

    override fun onClickTweetItem(position: Int) {
        val (_, _, _, tweet)= adapter.data[position]
        tweet?.let {
            MaterialAlertDialogBuilder(getContext())
                .setTitle("Do you want to tweet it?")
                .setMessage(tweet)
                .setPositiveButton("Confirm"){
                        dialog, which ->
                    //  tweet it
                }
                .setNegativeButton("Cancel"){
                        dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun getViewLifecycleOwner(): LifecycleOwner =
        singleActivityController.getActivity()

    private fun getContext() = singleActivityController.getActivity()

    private fun fabBtnClicked() {
        getRootView().fab.setOnClickListener {
            listeners.forEach {
                it.onFabBtnClicked()
            }
        }
    }
}