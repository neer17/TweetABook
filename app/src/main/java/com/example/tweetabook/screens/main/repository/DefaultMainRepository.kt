package com.example.tweetabook.screens.main.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tweetabook.api.MyBackendApi
import com.example.tweetabook.db.daos.TweetDAO
import com.example.tweetabook.db.entities.TweetEntity
import com.example.tweetabook.firebase.deleteAllFiles
import com.example.tweetabook.firebase.filesCount
import com.example.tweetabook.firebase.uploadFile
import com.example.tweetabook.screens.main.repository.Jobs.UploadAndConversionJob
import com.example.tweetabook.socket.MySocket
import com.example.tweetabook.socket.responses.ErrorResponse
import com.example.tweetabook.socket.responses.ServerResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DefaultMainRepository
@Inject
constructor(
    val mySocket: MySocket,
    val tweetDAO: TweetDAO,
    val networkPresent: Boolean,
    val backendApi: MyBackendApi
) : MainRepository {
    private val TAG = "AppDebug: MainRepository"

    private val jobList: MutableLiveData<ArrayList<Jobs>> by lazy {
        MutableLiveData<ArrayList<Jobs>>()
    }

    private val internalJobList = ArrayList<Jobs>()
    private var anyPendingJobs: Boolean = false


    override fun addJob(job: Jobs) {
        Log.d(TAG, "addJob: ")
        internalJobList.add(job)
        jobList.value = internalJobList
    }

    override fun removeJob() {
        Log.d(TAG, "removeJob: ")
        if (!internalJobList.isNullOrEmpty()) internalJobList.removeAt(0)
        anyPendingJobs = false
        jobList.value = internalJobList
    }

    override suspend fun executeJob(job: Jobs) {
        if (!networkPresent) {
            // TODO: show toast
            return
        }

        when (job) {
            is UploadAndConversionJob -> {
                anyPendingJobs = true
                coroutineScope {
                    val id = job.id
                    val localImageUri = job.localImageUri

                    val storageImageUri = uploadImage(id, localImageUri)
                    convertImage(id, storageImageUri)
                }
            }
            is Jobs.ConversionJob -> {
                anyPendingJobs = true
                convertImage(job.id, job.storageImageUri)
            }
        }
    }

    private suspend fun uploadImage(id: String, localImageUri: String): String =
        withContext(Dispatchers.IO) {
            val storageImageUri = uploadFile(localImageUri)

            val updatedTweetCount = tweetDAO.updateTweet(
                TweetEntity(
                    id = id,
                    imageUri = storageImageUri.toString(),
                    imageUploaded = true
                )
            )
            storageImageUri.toString()
        }

    private suspend fun convertImage(id: String, storageImageUri: String) {
        withContext(Dispatchers.IO) {
            storageImageUri.let {
                val json = JsonObject()
                json.addProperty("id", id)
                json.addProperty("uri", it)
                socketEmitEvent(json)
            }
        }
    }


    override suspend fun getFilesCount(): Int? {
        return filesCount()
    }

    override suspend fun emptyStorage() {
        deleteAllFiles()
    }

    override suspend fun deleteAllTweets() {
        val allTweets = tweetDAO.getAllTweetsWithOutLiveData()
        allTweets?.let {
            val tweetsDeleted = tweetDAO.deleteAll(allTweets)
            Log.d(TAG, "deleteAllTweets: tweetsDeleted: $tweetsDeleted")
        }
    }

    override fun socketEmitEvent(json: JsonObject) {
        mySocket.socketEmitEvent(json)
    }

    override suspend fun tweet(tweetObject: JsonObject): Boolean {
        var isTweeted = false

        withContext(Dispatchers.IO) {
            val response = backendApi.sendTweet(tweetObject).execute()
            if (response.isSuccessful) {
                response.body()?.let {
                    isTweeted = true
                }
            } else {
                Log.e(TAG, "tweet server error: ", Throwable(response.errorBody().toString()))
                isTweeted = false
            }
        }

        return isTweeted
    }

    override fun exposeServerResponse(): LiveData<ServerResponse> {
        return mySocket.socketResponse
    }

    override fun exposeServerError(): LiveData<ErrorResponse> {
        return mySocket.serverErrorResponse
    }

    override fun exposeJobList(): LiveData<ArrayList<Jobs>> = jobList

    override fun exposeAnyPendingJobs() = anyPendingJobs
}

sealed class Jobs() {
    class UploadAndConversionJob(val id: String, val localImageUri: String) : Jobs()

    class ConversionJob(val id: String, val storageImageUri: String) : Jobs()
}