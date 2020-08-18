package com.example.tweetabook.screens.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.tweetabook.R
import com.example.tweetabook.common.Constants
import com.example.tweetabook.db.daos.TweetDAO
import com.example.tweetabook.db.entities.TweetEntity
import com.example.tweetabook.mappers.TweetMappers
import com.example.tweetabook.screens.main.adapter.MyAdapter
import com.example.tweetabook.screens.main.repository.Jobs.ConversionJob
import com.example.tweetabook.screens.main.repository.Jobs.UploadAndConversionJob
import com.example.tweetabook.screens.main.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.JsonObject
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), MyAdapter.AdapterOnClickListener {
    private val TAG = "AppDebug: MainFragment"

    @Inject
    lateinit var tweetDAO: TweetDAO

    @Inject
    lateinit var tweetMappers: TweetMappers

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var adapter: MyAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView()
        registerClickListeners()
        subscribers()
    }

    private fun subscribers() {
        /*
        * fired multiple times, initially when the app starts, then again multiple times when the tweets gets updated
        */
        tweetDAO.getAllTweets().observe(viewLifecycleOwner, Observer { tweets ->
            tweets?.forEach {
                val adapterDataClass = tweetMappers.mapFromEntity(it)
                val dataPresent = adapter.data.contains(adapterDataClass)

                //  whenever tweet is updated
                if (dataPresent)
                    adapter.updateData(adapterDataClass)
                else {
                    adapter.addData(adapterDataClass)

                    val (id, imageUri, imageUploaded, _, imageConverted, _) = it

                    if (!imageUploaded) {
                        viewModel.addJob(UploadAndConversionJob(id, imageUri))
                    } else if (!imageConverted) {
                        viewModel.addJob(ConversionJob(id, imageUri))
                    }
                }
            }

            if (tweets == null || tweets.isEmpty())
                adapter.clearData()
        })


        viewModel.serverResponse.observe(viewLifecycleOwner, Observer {
            //  changing progress in adapter
            val (id, progress, status, tweet) = it

            //  remove the job from "jobList" when translation is done, updating the local db entry of tweet
            if (status == Constants.IMAGE_CONVERSION_COMPLETED) {
                //  REMOVING the first job from ArrayList
                lifecycleScope.launch(Dispatchers.IO) {
                    tweet?.let {
                        Log.d(TAG, "subscribers: image conversion completed: ")

                        tweetDAO.getTweetById(id)?.let { currentTweet ->
                            currentTweet.imageConverted = true
                            currentTweet.tweet = tweet

                            //  updating tweet in db then removing the job
                            val tweetUpdated = tweetDAO.updateTweet(currentTweet)
                            val message =
                                if (tweetUpdated == 1) "tweet updated" else "tweet didn't get updated"
                            Log.d(TAG, "subscribers: $message")

                            withContext(Dispatchers.Main) {
                                viewModel.removeJob()
                            }
                        }

                    }
                }
            }

            lifecycleScope.launch(Dispatchers.Main) {
                delay(200)
                adapter.updateProgress(id, progress, status, tweet)
            }
        })

        viewModel.jobList.observe(viewLifecycleOwner, Observer {
            it?.let {
                val anyPendingJob = viewModel.getPendingJobStatus()

                Log.d(
                    TAG,
                    "subscribers: total jobs left: ${it.size} \t pending jobs: $anyPendingJob"
                )

                if (!anyPendingJob && it.size > 0) {
                    val job = it[0]
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.executeJob(job)
                    }
                }
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

                //  INSERT local db
                lifecycleScope.launch(Dispatchers.IO) {
                    tweetDAO.insertTweet(initialTweetsEntity)
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.e(TAG, "onActivityResult: result.error", error)
            }
        }
    }

    override fun onClickTweetItem(position: Int) {
        val (id, _, _, _, tweet) = adapter.data[position]
        tweet?.let {
            MaterialAlertDialogBuilder(getContext())
                .setTitle("Do you want to tweet it?")
                .setMessage(tweet)
                .setPositiveButton("Confirm") { dialog, which ->
                    lifecycleScope.launch {
                        val tweetObject = JsonObject()
                        tweetObject.addProperty("tweet", tweet)
                        val isTweeted = viewModel.tweet(tweetObject)
                        if (isTweeted) {
                            val currentTweet = tweetDAO.getTweetById(id)
                            currentTweet?.let {
                                it.tweeted = true
                                val tweetUpdated = tweetDAO.updateTweet(it) > 1
                                Log.d(
                                    TAG,
                                    "onClickTweetItem: tweet updated after tweeting: $tweetUpdated"
                                )
                            }
                        }
                    }
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
        adapter = MyAdapter(requireActivity() as Context, lifecycleScope, this)
        val recyclerView = recycler_view
        recyclerView.adapter = adapter
        val decoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        recyclerView.addItemDecoration(decoration)
    }
}