package com.example.tweetabook.screens.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.tweetabook.R
import com.example.tweetabook.screens.main.responses.TweetResponse
import com.example.tweetabook.screens.main.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), MyAdapter.AdapterOnClickListener {
    private val TAG = "AppDebug: MainFragment"

    private lateinit var adapter: MyAdapter

    private val viewModel: MainViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registerClickListeners()
        recyclerView()
        subscribers()
    }


    private fun subscribers() {
        viewModel.serverResponse.observe(viewLifecycleOwner, Observer {
            //  changing progress in adapter
            val (id, progress, status, tweet) = it
            lifecycleScope.launch(Dispatchers.Main) {
                adapter.updateProgress(id, progress, status, tweet)
            }
        })
    }

    private fun recyclerView() {
        adapter = MyAdapter(this)
        val recyclerView = recycler_view
        recyclerView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri = result.uri

                val id = UUID.randomUUID().toString()
                val tweetResponse = TweetResponse(id, resultUri)
                adapter.addData(tweetResponse)

                // upload image and set adapter
                viewModel.uploadImageAndSendDownloadUriToServer(id, resultUri)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.e(TAG, "onActivityResult: result.error", error)
            }
        }
    }

    override fun onClickTweetItem(position: Int) {
        val (_, _, _, tweet) = adapter.data[position]
        tweet?.let {
            MaterialAlertDialogBuilder(getContext())
                .setTitle("Do you want to tweet it?")
                .setMessage(tweet)
                .setPositiveButton("Confirm") { dialog, which ->
                    //  tweet it
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun registerClickListeners() {
        //  FAB button
        fab.setOnClickListener {
            CropImage.activity()
                .start(requireContext(), this);
        }
    }
}