package com.example.tweetabook.screens.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.tweetabook.R
import com.example.tweetabook.common.Constants
import com.example.tweetabook.db.daos.TweetDAO
import com.example.tweetabook.db.entities.TweetEntity
import com.example.tweetabook.mappers.TweetMappers
import com.example.tweetabook.screens.main.adapter.MyAdapter
import com.example.tweetabook.screens.main.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), MyAdapter.AdapterOnClickListener {
    private val TAG = "AppDebug: MainFragment"

    @Inject
    lateinit var tweetDAO: TweetDAO

    @Inject
    lateinit var tweetMappers: TweetMappers

    private lateinit var adapter: MyAdapter

    private val viewModel: MainViewModel by activityViewModels()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView()
        registerClickListeners()
        subscribers()
    }


    private fun subscribers() {
        tweetDAO.getAllTweets().observe(viewLifecycleOwner, Observer { tweets ->
            tweets?.forEach {
                val adapterDataClass = tweetMappers.mapFromEntity(it)
                val dataPresent = adapter.data.contains(adapterDataClass)

                Log.d(
                    TAG,
                    "subscribers: data present: $dataPresent \t adapterdataclass: $adapterDataClass \n"
                )
                if (dataPresent) {
                    adapter.updateData(adapterDataClass)
                } else
                    adapter.addData(adapterDataClass)
            }
        })


        viewModel.serverResponse.observe(viewLifecycleOwner, Observer {
            //  changing progress in adapter
            val (id, progress, status, tweet) = it

            Log.d(TAG, "subscribers: id: $id \t status: $status \t progress: $progress \n")

            //  remove the job from "jobList" when translation is done, updating the local db entry of tweet
            if (status == Constants.IMAGE_CONVERSION_COMPLETED && progress == 1.0) {
                viewModel.removeJobWhenTranslationIsDone(id)
                lifecycleScope.launch(Dispatchers.IO) {
                    tweet?.let {
                        tweetDAO.updateTranslatedText(id, it)
                    }
                }
            }

            lifecycleScope.launch(Dispatchers.Main) {
                adapter.updateProgress(id, progress, status, tweet)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val localImage: String = result.uri.toString()

                val id = UUID.randomUUID().toString()
                val initialTweetsEntity = TweetEntity(id, localImage)

                //  local db entry
                lifecycleScope.launch(Dispatchers.IO) {
                    tweetDAO.insertTweet(initialTweetsEntity)
                }

                // upload image and set adapter
                viewModel.uploadToStorageAndTranslate(id, localImage)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.e(TAG, "onActivityResult: result.error", error)
            }
        }
    }

    override fun onClickTweetItem(position: Int) {
        val (_, _, _, _, tweet) = adapter.data[position]
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

    private fun recyclerView() {
        adapter = MyAdapter(this)
        val recyclerView = recycler_view
        recyclerView.adapter = adapter
    }
}